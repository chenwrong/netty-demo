package com.hqu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取实际数据，客户端发送的数据就是msg
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将任务提交到eventLoop中的taskQueue任务队列中异步执行  taskQueue只有一个线程来执行任务队列中的任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("服务端处理消息中", CharsetUtil.UTF_8));
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("服务端收到消息："+buf.toString(CharsetUtil.UTF_8));
                System.out.println("客户端地址:" + ctx.channel().remoteAddress());
            }
        });
        // 提交定时任务到eventLoop 中的scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("定时任务执行");
            }
        },8, TimeUnit.SECONDS);
    }
    //数据读取完毕的回调方法,回写信息给客户端通道
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8));

    }
}
