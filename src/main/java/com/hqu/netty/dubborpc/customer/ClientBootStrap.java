package com.hqu.netty.dubborpc.customer;

import com.hqu.netty.dubborpc.netty.NettyClient;
import com.hqu.netty.dubborpc.publicinterface.HelloService;

public class ClientBootStrap {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        HelloService bean = (HelloService)client.getBean(HelloService.class, "HelloService#hello#");
        String res = bean.hello("hello world");
        System.out.println(res);
    }
}
