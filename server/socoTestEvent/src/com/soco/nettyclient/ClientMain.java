package com.soco.nettyclient;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.log.Log;
import com.soco.test.cases.TestCase;
import com.soco.test.cases.TestCaseFile;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class ClientMain {

    static String HOST = System.getProperty("host", "52.26.115.134");
    //static String HOST = System.getProperty("host", "127.0.0.1");
    static int PORT = Integer.parseInt(System.getProperty("port", "28992"));
    
    private TestCaseFile _tcf = null;
    private TestCase _current_case = null;
    
    private boolean wait_resposne = true;

    public static void main(String[] args) throws Exception {
        
    }
    
    public void init(String host, int port){
    	
    }
    
    public void start(TestCaseFile tcf) throws SSLException{
    	// Configure SSL.
        //final SslContext sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            
            this._tcf = tcf;
  
            if(this._tcf == null) {
            	Log.error("There are no test cases.");
            	return;
            }
            
            for (TestCase tcase : this._tcf.getListTestCase()) {
            	
            	if(tcase.getName().isEmpty() || tcase.getReqUrl().isEmpty()){
            		Log.warn("The case is null.");
            		continue;
            	}
            	Log.debug("Test case name: " + tcase.getName());
            	Log.debug("Send to URL: " + tcase.getReqUrl());
            	
            	URI uri = new URI(tcase.getReqUrl());
            	String scheme = uri.getScheme() == null? "http" : uri.getScheme();
                String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
                int port = uri.getPort();
                if (port == -1) {
                    if ("http".equalsIgnoreCase(scheme)) {
                        port = 8090;
                    } else if ("https".equalsIgnoreCase(scheme)) {
                        port = 8443;
                    }
                }

                if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                    System.err.println("Only HTTP(S) is supported.");
                    return;
                }
                
                Log.debug("host: " + host + " . port: " + port);
                Log.debug("request json: " + tcase.getReqJson());

                // Configure SSL context if necessary.
                final boolean ssl = "https".equalsIgnoreCase(scheme);
                final SslContext sslCtx;
                if (ssl) {
                    sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                } else {
                    sslCtx = null;
                }
            	try {
                    Bootstrap b = new Bootstrap();
                    b.group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new ClientInitializer(sslCtx, this));

                    // Make the connection attempt.
                    Channel ch = b.connect(host, port).sync().channel();

                    // Prepare the HTTP request.
                    HttpMethod hMethod = new HttpMethod(tcase.getReqMethod());
                    FullHttpRequest request = new DefaultFullHttpRequest(
                            HttpVersion.HTTP_1_1, hMethod, uri.getRawPath());
                    request.headers().set(HttpHeaders.Names.HOST, host);
                    request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                    request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
                    request.headers().add(HttpHeaders.Names.CONTENT_TYPE, "application/json");
                    
                    //
                    tcase.setReqJson(this._tcf.getTestcaseVariable().replaceVariables(tcase.getReqJson()));
                    //
                    ByteBuf bbuf = Unpooled.copiedBuffer(tcase.getReqJson().toString(), StandardCharsets.UTF_8);
                    Log.debug("content length: " + bbuf.readableBytes());
                    request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, bbuf.readableBytes());
                    request.content().clear().writeBytes(bbuf);

                    this.setCurrentCase(tcase);

                    // Send the HTTP request.
                    ch.writeAndFlush(request);
                    
                    this.setWaitResposne(true);
                    
                    while (this.isWaitResposne()){
                    	Log.debug("In waiting response, to sleep 0.5 seconds");
                    	Thread.sleep(500);
                    }

                    // Wait for the server to close the connection.
                    ch.closeFuture().sync();
                } finally {
                    // Shut down executor threads to exit.
                   // group.shutdownGracefully();
                }
            	
            }

        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
            System.out.println("bye!");
        }
    }
    
    public void setTestCaseResp(String resp){
    	try {
    		JSONObject json = new JSONObject(resp);
			this.getCurrentCase().setRespJson(json);
			this._tcf.getTestcaseVariable().parseResponseVariables(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	public TestCase getCurrentCase() {
		return _current_case;
	}

	public void setCurrentCase(TestCase _current_case) {
		this._current_case = _current_case;
	}

	public boolean isWaitResposne() {
		return wait_resposne;
	}

	public void setWaitResposne(boolean wait_resposne) {
		this.wait_resposne = wait_resposne;
	}
}
