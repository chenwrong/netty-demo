package com.hqu.netty.dubborpc.provider;

import com.hqu.netty.dubborpc.netty.NettyServer;

//启动一个服务者提供类
public class ServerBootStrap {
    public static void main(String[] args) throws Exception{
        NettyServer.start("127.0.0.1", 8888);
    }
}
