package com.hqu.netty.group;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//客户端
public class GroupChatClient {
    private final String host;
    private final int port;
    public GroupChatClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
        NioEventLoopGroup eventExecutors = null;
        try {
            eventExecutors = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder(),new StringEncoder(),new GroupChatClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()){
                String s = sc.nextLine();
                channel.writeAndFlush(s);
            }
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new GroupChatClient("localhost",6666).run();
    }

}
