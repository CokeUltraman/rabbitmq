package com.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    //1.注入rabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testHelloworld(){
        //2.发送消息
        rabbitTemplate.convertAndSend("spring_queue","helloworld spring");
    }
//    发送fanout消息
    @Test
    public void testFanout(){
        //2.发送消息
        rabbitTemplate.convertAndSend("spring_fanout_exchange","","spring fanout...");
    }

    @Test
    public void testTopic(){
        //2.发送消息
        rabbitTemplate.convertAndSend("spring_topic_exchange","heima.haha.shabi","spring topic...");
    }
}
