package main;

/**
 * Desc: Game Proto : Server : Main class
 * Coder: elGringo
 * Date: 21.06.2010
 * Time: 18:07:28
 * Copyright MetroDezDG (c) 2008
 */
import core.NettyServer.NettyServer;
import core.Sql;
import core.Utils;
import core.coreConfig;
import main.Game.Main;
import org.w3c.dom.Node;

import java.io.FileInputStream;

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
            
            if (props.get("dbUrl")!="") {
            	Sql sql = Sql.getInstance();
            	sql.Init((String) props.get("dbURL"), (String) props.get("dbUser"),
            			(String) props.get("dbPass"), (String) props.get("dbMaxConnections"),
            			(String) props.get("dbDriver"));           
            }
            new NettyServer(Integer.parseInt(props.get("userport")), new Main("Game", "Main"), 16);

            //Utils.log("Local ip is "+coreConfig.getInstance().get("localIp"));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}