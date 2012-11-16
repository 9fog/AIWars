package Sandbox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;

import net.minidev.json.JSONObject;

public class Connector
{
  private final Socket _socket;
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;
  private final Connector.SocketWrapper _wrapper;
  private final Simulator _simulator;

  public Connector()
    throws IOException
  {
     this._socket = new Socket("192.168.1.95", 23001);

     this.socketReader = new BufferedReader(new InputStreamReader(this._socket.getInputStream(), "UTF-8"));
     this.socketWriter = new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream(), "UTF-8"));

     new Thread(new Connector.Receiver()).start();
     this._wrapper = new Connector.SocketWrapper();
     this._wrapper.start();

     this._simulator = new Simulator(this);
     
     //Send initialization command
     JSONObject cmd = new JSONObject();
     cmd.put("_op", "testFight");
     cmd.put("botsCount", Config.BOTS.length);
     cmd.put("mapName", Config.MAP_NAME);
     cmd.put("maxTicks", Config.COMBAT_MAX_TICKS);
     send (cmd.toJSONString());
  }

  public void send(String msg)
  {
     this._wrapper.addInQueue(msg);
     log("SND > " + msg);
  }

  public synchronized void close()
  {
     if (!this._socket.isClosed())
      try {
         this._socket.close();
         System.exit(0);
      } catch (IOException ignored) {
         ignored.printStackTrace();
      }
  }

  public static void log(String activity)
  {
     Calendar cal = Calendar.getInstance();
     activity = "[" + cal.get(5) + 
       "." + (cal.get(2) + 1) + 
       " " + 
       cal.get(11) + 
       ":" + cal.get(12) + 
       ":" + cal.get(13) + 
       "] " + activity;
     activity = activity + "\n";

     System.out.print(activity);
  }

  private class Receiver extends Thread
  {
    public void run()
    {
       Connector.log("Reciever started...");
       while (!Connector.this._socket.isClosed())
        try {
           char[] charBuffer = new char[4096];

           while (Connector.this.socketReader.read(charBuffer, 0, 1) != -1) {
             StringBuilder sb = new StringBuilder();
             String data = "";

             while ((charBuffer[0] != 0) && (sb.length() < 4096)) {
               sb.append(charBuffer[0]);
               Connector.this.socketReader.read(charBuffer, 0, 1);
            }

             data = sb.toString();
             Connector.log("RCV > " + data);

             Connector.this._simulator.processServerCommand(data);

             sleep(100);
          }
        }
        catch (IOException e) {
           if ("Socket closed".equals(e.getMessage())) {
            break;
          }
           Connector.log("Connection lost");
           Connector.this.close();
        } catch (Exception e) {
           e.printStackTrace();
        } 
    }
  }

  private class SocketWrapper extends Thread {
     public int qStartPoint = 0;
     public int qStopPoint = 0;
     public int qMaxIndex = 500;
     public String[] Queue = new String[qMaxIndex + 1];
     private String message;

     public void run() { 
     	try { 
     		Connector.log("Transmitter started");
         while (true) {
           while (qStartPoint!=qStopPoint){   //Если указатели не равны, значит есть что отсылать
        	   synchronized (Queue) {
        		   message = Queue[qStartPoint];
        		   Queue[qStartPoint] = null;
        	   }
        	   try {
        		   socketWriter.write(message + "\0");
        		   socketWriter.flush();
        	   }
        	   catch (Exception e) {
        		   e.printStackTrace();
        		   close();
        	   }

        	   qStartPoint += 1;
        	   if (qStartPoint > qMaxIndex) {
        		   qStartPoint = 0;
        	   }           
           
        	   try
        	   {
        		   sleep(100);
        	   } catch (InterruptedException e) {
        		   e.printStackTrace();
        	   }
           }
           
            while (qStartPoint == qStopPoint) {
             try {
                sleep(100);
             } catch (InterruptedException e) {
                e.printStackTrace();
             }
           }
         } 
       } catch (NullPointerException e) {
          e.printStackTrace();
       } 
    }

    public void addInQueue(String d)
    {
       synchronized (this) {
         Queue[qStopPoint] = d;
         qStopPoint ++;
         if (qStopPoint > qMaxIndex) {
           qStopPoint = 0;
         }
         
         if (qStartPoint == qStopPoint) {
           Connector.log("Error: wrapper owerflow");
           close();
        }
      }
    }
  }
}
