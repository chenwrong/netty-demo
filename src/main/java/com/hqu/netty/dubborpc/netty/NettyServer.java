package com.hqu.netty.dubborpc.netty;

import com.hqu.netty.coder.MyByteToLongDecoder;
import com.hqu.netty.coder.MyLongToByteHandler;
import com.hqu.netty.coder.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
    public static void start(String hostname, int port) throws Exception{
        start0(hostname, port);
    }
    //完成nettyServer的初始化工作
    private static void start0(String hostname, int port) throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder(),new StringEncoder());
                            pipeline.addLast(new NettyServerHandler()); //业务处理

                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(hostname,port).sync();
            //监听关闭，同步阻塞主线程，否则直接执行finally代码块把服务器关闭。客户端无法连接
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
