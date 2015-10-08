package com.soco.app.netserver;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soco.XML.XmlConfig;
import com.soco.app.exchange.AppMessageDispatch;
import com.soco.app.httpserver.AppHttpServer;



public class AppNetServer {
	
	private static final Logger log = LogManager.getRootLogger();
	
	private static String config_xml_file = "config.xml";
    static int PORT = Integer.parseInt(System.getProperty("port", "28992"));
    private static long munit = 10000000000000000l;

    public static void main(String[] args) throws Exception {
    	
        log.info("=== App Server Start ===");
        log.info(System.getProperty("user.dir"));
        String configXml = AppNetServer.config_xml_file;
        if(args.length > 0){
            configXml = args[0];
        }
        log.info("Load " + configXml + " config file...");
        XmlConfig config = new XmlConfig(configXml);
        config.setServerName("HeartBeatServer").setConnectionNode("TCPConnection").parse();

        if(0 != config.getPort()){
            PORT = config.getPort();
        }
        log.info("Port is "+PORT);
        long mm = (new Date()).getTime();
        log.info((new Date()).toString() + " .milliseconds: "+mm);
        mm = 1 * munit + mm;
        log.info("uid: " + mm);
        Date dd = new Date();
        dd.setTime(90000000000000l);
        log.info(dd.toString()+ " : " + dd.getTime());
        //
        String sl = "1443363623179";
        long ll = Long.parseLong(sl);
        dd.setTime(ll);
        log.info(dd.toString());
        ////
        AppMessageDispatch.initHandler();
        ////------->
        AppHttpServer.startServer();
    }
}
