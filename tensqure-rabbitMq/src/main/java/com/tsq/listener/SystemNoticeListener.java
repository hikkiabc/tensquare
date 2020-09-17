package com.tsq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tsq.netty.MyWebSocketHandler;
import com.tsq.utils.R;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.HashMap;
import java.util.Map;

public class SystemNoticeListener implements ChannelAwareMessageListener {
    private static ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String consumerQueue = message.getMessageProperties().getConsumerQueue();
        String userId = consumerQueue.substring(consumerQueue.lastIndexOf("_") + 1);
        io.netty.channel.Channel userChannel = MyWebSocketHandler.channelMap.get(userId);
        if (null!=userChannel){
         Map map=   new HashMap<>();
                 map.put("systemNoticeCount",1);
            R data = R.data(map);
            userChannel.writeAndFlush(
                    new TextWebSocketFrame(objectMapper.writeValueAsString(data)));
        }
    }
}
