package com.itcast.producer;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

//发送消息
public class Producer_Helloworld {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory=new ConnectionFactory();
        //2.设置参数
        factory.setHost("192.168.253.129");//ip 默认值为localhost
        factory.setPort(5672);//端口 默认值也是5672
        factory.setVirtualHost("/itcast");//虚拟机 默认值/
        factory.setUsername("heima");//用户名 默认值guest
        factory.setPassword("heima");//密码 默认值guest


        //3.创建连接Connection
        Connection connection = factory.newConnection();
        //4.创建Channel
        Channel channel = connection.createChannel();
        //5.创建队列Queue
        //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        //queue:队列名称   durable是否持久化 当mq重启，还在     exclusive:是否独占 只能有一个消费者监听这队列 当connection关闭时，是否删除队列
        //autoDelete 是否自动删除，当没有Consumer,自动删除掉   argument:参数
        //如果没有叫helloworld队列会自动创建
        channel.queueDeclare("hello_world",true,false,false,null);

        //6.发送消息
        //exchange:交换机名称，简单模式会使用默认的。 routingkey:路由信息
        //props:配置信息 body:发送消息数据
        String body="hello rabbitmq";
        channel.basicPublish("","hello_world",null,body.getBytes());
        //7.释放资源
        channel.close();
        connection.close();
    }
}
