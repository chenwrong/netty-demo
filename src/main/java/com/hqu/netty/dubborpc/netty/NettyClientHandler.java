package com.hqu.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    private String result;
    private String para;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = (String) msg;  //得到调用服务端传回的结果
        notify();
    }

    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        wait();
        return result;
    }

    public void setPara(String para) {
        this.para = para;
    }
}
