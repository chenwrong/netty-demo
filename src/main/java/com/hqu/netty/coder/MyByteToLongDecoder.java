package com.hqu.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * @param ctx 上下文
     * @param byteBuf 入栈的信息
     * @param list 解码后的数据，会传递给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //8个字节转换成Long数据
        System.out.println("解码器被调用");
        if (byteBuf.readableBytes()>=8){
            list.add(byteBuf.readLong());
        }
    }
}
