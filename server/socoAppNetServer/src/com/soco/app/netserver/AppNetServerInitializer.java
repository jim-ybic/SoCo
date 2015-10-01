package com.soco.app.netserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;


public class AppNetServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public AppNetServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        if ( sslCtx != null){
        	pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        // On top of the SSL handler, add the text line codec.
        //pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        //pipeline.addLast(new StringDecoder());
        //pipeline.addLast(new StringEncoder());
        
        pipeline.addLast(new HttpServerCodec());

        // and then business logic.
        pipeline.addLast(new AppNetServerHandler());
    }
}