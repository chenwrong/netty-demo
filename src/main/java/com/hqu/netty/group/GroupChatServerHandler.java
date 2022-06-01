package com.hqu.netty.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    //一旦连接建立，第一个被执行的方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 向channelGroup中的所有channel发送消息
        channelGroup.writeAndFlush("[用户]"+ctx.channel().remoteAddress()+" 加入了聊天室");
        channelGroup.add(ctx.channel());
    }
    //channel断开连接,通知其余所有channel
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[用户]"+ctx.channel().remoteAddress() + " 离开了聊天室");
    }

    //表示channel处于活跃状态，提示XXX上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().eventLoop());
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 下线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch){
                //不是当前channel，直接转发消息
                ch.writeAndFlush("[用户]"+channel.remoteAddress() + ":" + s);
            }else{
                //回显给自己
                ch.writeAndFlush("[我]:"+s);
            }
        });
    }
}
