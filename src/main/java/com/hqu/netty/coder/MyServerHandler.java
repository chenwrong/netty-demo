package com.hqu.netty.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("服务端接收数据:"+aLong);
        channelHandlerContext.writeAndFlush(56789L); //发送给客户端
    }
}
