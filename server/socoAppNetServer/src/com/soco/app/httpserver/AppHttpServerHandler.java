package com.soco.app.httpserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Map.Entry;

import com.soco.app.exchange.AppMessageDispatch;
import com.soco.app.handler.AppMessageHandler;
import com.soco.log.Log;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.util.CharsetUtil;

public class AppHttpServerHandler extends ChannelInboundHandlerAdapter {
    private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;

            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            boolean keepAlive = HttpHeaders.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
            	/* testing code, please ignore this
            	String url = req.getUri();
            	List<Entry<String, String>> listHeader = req.headers().entries();
            	String content = req.content().toString(CharsetUtil.UTF_8);
            	HttpMethod hm = req.getMethod();
            	
            	for(int i=0;i<listHeader.size();i++){
            		Entry<String, String> headerEntry = listHeader.get(i);
            		Log.debug("head key:" + headerEntry.getKey() + " value:" + headerEntry.getValue());
            	}
            	
            	Log.debug("http method: " + hm.name());
            	Log.debug("url:" + url);
            	Log.debug("content:" + content);
            	// end testing code */
            	
            	AppMessageHandler msgHandler = AppMessageDispatch.startDispatch(req);
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                if (msgHandler != null){
                	ctx.writeAndFlush(msgHandler.getResponse());
                	Log.debug("Complemte read. send out response. ");
                } else {
                    ctx.writeAndFlush(response);
                }
                
            }
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	Log.debug("The channel inactive.");
    	ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
