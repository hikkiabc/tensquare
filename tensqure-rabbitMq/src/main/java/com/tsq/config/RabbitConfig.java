package com.tsq.config;

import com.tsq.listener.ArticleThumbNoticeListener;
import com.tsq.listener.SystemNoticeListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean("systemNoticeListener")
    public SimpleMessageListenerContainer simpleMessageListenerContainer(
            ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer
                = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        simpleMessageListenerContainer.setMessageListener(new SystemNoticeListener());
        return simpleMessageListenerContainer;
    }
    @Bean("articleThumbNoticeListener")
    public SimpleMessageListenerContainer articleThumbNoticeListener(
            ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer
                = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        simpleMessageListenerContainer.setMessageListener(new ArticleThumbNoticeListener());
        return simpleMessageListenerContainer;
    }
}
