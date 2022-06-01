package com.hqu.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteHandler extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf byteBuf) throws Exception {
        System.out.println("编码器被调用");
        byteBuf.writeLong(msg);
    }
}
