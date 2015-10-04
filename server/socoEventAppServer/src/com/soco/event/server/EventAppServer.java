package com.soco.event.server;

import com.soco.XML.XmlConfig;
import com.soco.app.exchange.AppMessageDispatch;
import com.soco.app.httpserver.AppHttpServer;
import com.soco.db.adapter.EventDBAdapter;
import com.soco.log.Log;


/*
 * Main entry
 * This is the main entry class
 * 1, read config.xml file 
 * 2, configure the servers which needed to be configured
 * 3, start event main server if there is something need to do
 * 4, initialize the message dispatch handlers to prepare receiving message
 * 5, start the net server to listen the clients 
 * */
public class EventAppServer {
	
	public static final String SERVER_NAME = "EventServer";

    public static void main(String[] args) {
        /* trace in */
    	Log.infor("Start Event App Server...");
        /* initialize and check environment */
        /* read configuration file */
    	EventAppServer.configureServer();
        /* if configuration file is fine then to configure server and start server */
        /* start event main */
        /* initialize the message dispatch handlers to prepare receiving message */
        AppMessageDispatch.initHandler();
        try {
        	/* start net server */
			AppHttpServer.startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void configureServer(){
        XmlConfig xmlConfig = new XmlConfig("./config.xml");
        /* set server name which configured in config file */
        xmlConfig.setServerName(SERVER_NAME);
        xmlConfig.parse();
        xmlConfig.testShowMapNameValue();
    }
}
