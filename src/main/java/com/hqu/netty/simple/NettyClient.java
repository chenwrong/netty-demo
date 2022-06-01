package com.hqu.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

//客户端
public class NettyClient {
    public static void main(String[] args) throws Exception{
        //事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        //创建客户端启动对象
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("client is ok");
        ChannelFuture f = bootstrap.connect("localhost", 6668).sync();
        f.channel().closeFuture().sync();

    }
}
