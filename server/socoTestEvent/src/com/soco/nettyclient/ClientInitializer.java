package com.soco.nettyclient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    private ClientMain _cMain = null;
    
    public ClientInitializer(SslContext sslCtx, ClientMain cMain) {
        this.sslCtx = sslCtx;
        this._cMain = cMain;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpClientCodec());
        //pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new HttpContentDecompressor());
        //pipeline.addLast(new HttpObjectAggregator(1048576));
        // to be used since huge file transfer
        //pipeline.addLast(new ChunkedWriteHandler());

        // On top of the SSL handler, add the text line codec.
       // pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
       // pipeline.addLast(new StringDecoder());
       // pipeline.addLast(new StringEncoder());

        // and then business logic.
        ClientHandler cHandler = new ClientHandler();
        cHandler.setClientMain(this._cMain);
        pipeline.addLast(cHandler);
    }
}
