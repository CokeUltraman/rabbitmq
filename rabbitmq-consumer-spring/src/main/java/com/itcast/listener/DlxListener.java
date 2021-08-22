package com.itcast.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

//

@Component
public class DlxListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println(new String(message.getBody()));

            //处理业务逻辑
            System.out.println("处理业务逻辑");

            //手动签收
            channel.basicAck(deliveryTag,true);
        } catch (Exception e) {
            //拒绝签收 第三个参数 不重回队列 如果设置为true 则消息重新回到queue,broker会重新发送该消息给消费端
            channel.basicNack(deliveryTag,true,false);

        }
    }
}
