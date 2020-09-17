package com.tsq.config;

import com.tsq.netty.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {
    @Bean
    public NettyServer nettyServer(){
        NettyServer nettyServer = new NettyServer();
       new Thread(() -> nettyServer.start(1233)).start();
       return nettyServer;
    }

}
