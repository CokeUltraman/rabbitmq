package com.itcast.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProductTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    确认模式：1.确认模式开启 conectionFactory 中开启 publisher-confirms="true"
    //2.在rabbitTemplate定义ConfirmCallback回调函数
    @Test
    public void testConfirm(){

        //三个参数：correlationData 相关配置信息
        //ack：exchange是否成功收到信息 true:成功 false 失败
        //cause:失败原因
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String casue) {
                if(ack){
                    System.out.println("接收成功"+casue);
                }else{
                    System.out.println("fail"+casue);
                }
            }
        });

        //3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","message confirm");
    }


//    回退模式：当消息发送给exchange后，exchange路由到queue失败时，才会执行return callback
    //1.开启回退模式 publisher-returns="true"
    //2.设置returncallback
    //3.设置exchange处理消息的模式1.如果消息没有路由到queue,则丢弃消息   2.如果消息没有路由到queue 返回给消息发送方Returncallback
    @Test
    public void testReturn(){

        //设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                //message 消息对象
                //i:错误码
                // s: 错误信息
                // s1:交换机
                // s2:路由键

            }
        });


        //3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","message confirm");



    }
    @Test
    public void testSend(){
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","message confirm");
        }
    }
//    TTL过期 1.队列统一过期  2.消息单独过期
    @Test
    public void testTTL(){
//        for (int i = 0; i < 10; i++) {
//            rabbitTemplate.convertAndSend("test_exchange_ttl","ttl.hehe","message ttl");
//        }

        //消息单独过期
        //消息后处理对象 设置一些消息的参数信息
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //1. 设置message的信息
                message.getMessageProperties().setExpiration("5000");//设置过期时间
                //2.返回该信息

                return message;
            }
        };

        rabbitTemplate.convertAndSend("test_exchange_ttl","ttl.hehe","message ttl",messagePostProcessor);
    }
    @Test
    public void testDlx(){
        //1.测试过期时间，死信消息
        rabbitTemplate.convertAndSend("test_exchange_dlx","test.dlx.haha","I am a message");
    }

    @Test
    public void testDelay(){

    }


}
