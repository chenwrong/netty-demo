package com.hqu.netty.dubborpc.provider;

import com.hqu.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        if (msg == null){
            return "服务端收到消息";
        }else{
            return "服务端收到消息:"+msg;
        }
    }
}
