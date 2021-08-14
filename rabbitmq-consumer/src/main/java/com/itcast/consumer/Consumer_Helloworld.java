package com.itcast.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_Helloworld {
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
        //6.接收消息
        //参数：queue 队列名称 autoAck 是否自动确认 callback: 回调对象
        Consumer consumer=new DefaultConsumer(channel){
            //回调方法 当收到消息后，会自动执行该方法
            // 1.consumerTag：标识 2. envelope：获取一些信息 交换机 路由key,3.properties:配置信息 4.body:数据
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(consumerTag);
            }
        };
        channel.basicConsume("hello_world",true,consumer);

        //不关闭资源
    }
}
