package com.hqu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //HttpObject 是客户端和服务器端通信数据被封装的类型
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {
            System.out.println(channelHandlerContext.pipeline().hashCode());
            System.out.println(httpObject.getClass());
            System.out.println("客户端地址:"+channelHandlerContext.channel().remoteAddress());
            ByteBuf res = Unpooled.copiedBuffer("hello my name is server 嘿嘿", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, res);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plant;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,res.readableBytes());
            channelHandlerContext.writeAndFlush(response); //写回给客户端通道
        }
    }


}
