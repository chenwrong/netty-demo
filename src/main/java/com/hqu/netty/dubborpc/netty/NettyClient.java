package com.hqu.netty.dubborpc.netty;

import com.hqu.netty.coder.MyByteToLongDecoder;
import com.hqu.netty.coder.MyClientHandler;
import com.hqu.netty.coder.MyLongToByteHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler handler;

    public Object getBean(Class<?> clazz, String providerName){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (handler == null) {
                    initClient();
                }
                handler.setPara(providerName + args[0]);
                return executor.submit(handler).get();
            }
        });
    }
    private static void initClient() throws InterruptedException {
        handler = new NettyClientHandler();
        NioEventLoopGroup group = null;
        try {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //加入出栈的编码 编码处理器以及自己定义的handler处理业务逻辑
                            pipeline.addLast(new StringDecoder(),new StringEncoder(),handler);
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();
            future.channel().closeFuture();
        }catch (Exception e){
            group.shutdownGracefully();
        }
    }
}
