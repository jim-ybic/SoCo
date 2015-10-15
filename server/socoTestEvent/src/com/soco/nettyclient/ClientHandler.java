package com.soco.nettyclient;

import org.json.JSONObject;

import com.soco.log.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;


public class ClientHandler extends SimpleChannelInboundHandler<HttpObject> {

	private ClientMain _cMain = null;
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
            throws Exception {
        // TODO Auto-generated method stub
        Log.debug("Received response. ");
        
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;

            System.err.println("STATUS: " + response.getStatus());
            System.err.println("VERSION: " + response.getProtocolVersion());
            System.err.println();
            
            if(this._cMain != null)
            	this._cMain.getCurrentCase().setRespStatus(response.getStatus().code());
            else
            	Log.debug("Not set the main instance");

            if (!response.headers().isEmpty()) {
                for (CharSequence name: response.headers().names()) {
                    for (CharSequence value: response.headers().getAll(name)) {
                        System.err.println("HEADER: " + name + " = " + value);
                    }
                }
                System.err.println();
            }

            if (HttpHeaders.isTransferEncodingChunked(response)) {
                System.err.println("CHUNKED CONTENT {");
            } else {
                System.err.println("CONTENT {");
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;

            System.err.print(content.content().toString(CharsetUtil.UTF_8));
            System.err.flush();
            
            if(this._cMain != null){
            	String cont = content.content().toString(CharsetUtil.UTF_8);
            	Log.debug("Content: " + cont);
            	this._cMain.setTestCaseResp(cont);
            }
            else
            	Log.debug("Not set the main instance");

            if (content instanceof LastHttpContent) {
                System.err.println("} END OF CONTENT");
                ctx.close();
            }
        }
    	
        ctx.close();
        
        ///
        if(this._cMain != null)
        	this._cMain.setWaitResposne(false);
        else
        	Log.debug("Not set the main instance");
    }
    
    
    public void setClientMain(ClientMain cMain){
    	this._cMain = cMain;
    }
}
