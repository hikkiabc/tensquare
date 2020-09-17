package com.tsq.netty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsq.utils.ApplicationContextUtil;
import com.tsq.utils.R;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap();
    RabbitTemplate rabbitTemplate = (RabbitTemplate) ApplicationContextUtil.getBean(RabbitTemplate.class);
    SimpleMessageListenerContainer systemNoticeListener
            = (SimpleMessageListenerContainer) ApplicationContextUtil.getBean("systemNoticeListener");
    SimpleMessageListenerContainer articleThumbNoticeListener
            = (SimpleMessageListenerContainer) ApplicationContextUtil.getBean("articleThumbNoticeListener");
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        JsonNode jsonNode = objectMapper.readTree(text);
        String userId = jsonNode.get("userId").asText();
        Channel channel = channelMap.get(userId);
        if (null == channel) {
            channel = channelHandlerContext.channel();
            channelMap.put(userId, channel);
        }
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        Properties queueProperties = rabbitAdmin.getQueueProperties("article_subscribe_" + userId);
        int queue_message_count = (int) (queueProperties != null ? queueProperties.get("QUEUE_MESSAGE_COUNT") : 0);
        Properties article_thumb_queue = rabbitAdmin.getQueueProperties("article_thumb_" + userId);
        int queue_message_count1 = (int) (article_thumb_queue != null ? article_thumb_queue.get("QUEUE_MESSAGE_COUNT") : 0);
        Map map = new HashMap<>();
        map.put("systemNotice", queue_message_count);
        map.put("articleThumbNotice", queue_message_count1);
        R data = R.data(map);
        channel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(data)));
        if (queue_message_count>0) rabbitAdmin.purgeQueue("article_subscribe_" + userId,true);
        if (queue_message_count1>0) rabbitAdmin.purgeQueue("article_thumb_" + userId,true);
        systemNoticeListener.addQueueNames("article_subscribe_" + userId);
        articleThumbNoticeListener.addQueueNames("article_thumb_"+userId);
    }
}
