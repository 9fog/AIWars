package sandbox;
public class Starter
{
  public static void main(String[] args)
  {
    try
    {
       new Connector();
    } catch (Exception e) {
       e.printStackTrace();
       
       System.out.println(">");
       System.out.println("> If coonection cannot be established - please check Config.java class for right net settings");
    }
  }
}
