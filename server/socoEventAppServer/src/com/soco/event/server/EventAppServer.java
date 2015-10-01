package com.soco.event.server;

import com.soco.app.exchange.AppMessageDispatch;
import com.soco.app.httpserver.AppHttpServer;
import com.soco.log.Log;


/*
 * Main entry
 * */
public class EventAppServer {

    public static void main(String[] args) {
        /* trace in */
    	Log.infor("Start Event App Server...");
        /* initialize and check environment */
        /* read configuration file */
        /* if configuration file is fine then to configure server and start server */
        /* start event main */
        /* start net server */
        AppMessageDispatch.initHandler();
        try {
			AppHttpServer.startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
