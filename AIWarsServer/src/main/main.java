package main;

/**
 * Desc: Game Proto : Server : Main class
 * Coder: elGringo
 * Date: 21.06.2010
 * Time: 18:07:28
 * Copyright MetroDezDG (c) 2008
 */
import java.io.*;

import main.Game.Main;

import org.w3c.dom.*;

import core.*;
import core.NettyServer.NettyServer;

public class main {
	
    public static void main(final String[] args){                
        String config="config.xml";
        
        coreConfig props = coreConfig.getInstance();
        try{
            //Read config
             org.w3c.dom.Document d=javax.xml.parsers.DocumentBuilderFactory.newInstance().
                     newDocumentBuilder().parse(new FileInputStream(config));
             Node n=d.getFirstChild();
             if(n.getNodeName().equalsIgnoreCase("serverConfig"))n=n.getFirstChild();
             while(n!=null&&(n.getNodeType()!=Node.ELEMENT_NODE||!n.getNodeName().equalsIgnoreCase("server")))n=n.getNextSibling();
             if(n==null)throw new NullPointerException("'server' node not found");
             n=n.getFirstChild();
             while(n!=null){
                 if(n.getNodeType()==Node.ELEMENT_NODE&&n.getFirstChild()!=null)props.put(n.getNodeName(), n.getFirstChild().getNodeValue());
                 n=n.getNextSibling();
             }

            //==== Start all servers and services ==========================
             Utils.log("Server is starting...");

             Utils.log("main: Running services...");                          
                          
             /*
             Sql sql = Sql.getInstance();
             sql.Init((String) props.get("dbURL"), (String) props.get("dbUser"),
                     (String) props.get("dbPass"), (String) props.get("dbMaxConnections"),
                     (String) props.get("dbDriver"));
                     */             
               
             /*
             if (coreConfig.getInstance().get("useRedis").equalsIgnoreCase("1")) {
            	 Redis redis = Redis.getInstance();
            	 redis.Init((String) props.get("redisUrl"), Integer.parseInt(props.get("redisPort")), Integer.parseInt(props.get("redisConnections")), 0);
             }
             
             if (coreConfig.getInstance().get("useMongo").equalsIgnoreCase("1")) {
                 myMongo.getInstance().init((String)props.get("mongoIP"), Integer.parseInt(props.get("mongoPort")), (String)props.get("mongoDB"), 10);                         	 
             }
             */
             //myMongo.getInstance().init("localhost", 27017, "testbase", 10);             
             
             /*
             combatServer.getInstance();
             Router router = new Router(props.get("defaultPackage"), props.get("defaultRoom"));
             router.setDefaultCombat(new Main("Defender", "Main"));
             Router.init(router);
             */
             
             new NettyServer(Integer.parseInt(props.get("userport")), new Main("Game", "Main"), 16);
             
             //Utils.log("Local ip is "+coreConfig.getInstance().get("localIp"));
         }catch(Exception ex){
             ex.printStackTrace();
         }           
    }    
}