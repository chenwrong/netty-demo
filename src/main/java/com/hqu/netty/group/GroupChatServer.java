package com.hqu.netty.group;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class GroupChatServer {
    private int port; //监听端口
    public GroupChatServer(int port){
        this.port = port;
    }

    //监听客户端请求
    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //向pipeline加入解码器和编码器以及自己的业务处理handler
                            socketChannel.pipeline().addLast(new StringDecoder())
                                                    .addLast(new StringEncoder())
                                                    .addLast(new GroupChatServerHandler());

                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //监听关闭，同步阻塞主线程，否则直接执行finally代码块把服务器关闭。客户端无法连接
            channelFuture.channel().closeFuture().sync();  
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new GroupChatServer(6666).run();
    }
}
