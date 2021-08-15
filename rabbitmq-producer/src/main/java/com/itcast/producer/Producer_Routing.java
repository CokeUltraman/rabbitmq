package com.itcast.producer;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//发送消息
public class Producer_Routing {
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
        //5.创建交换机
        //参数：exchange:交换机名称 type:交换机类型（direct:定向 fanout:扇形广播 发送消息到每一个队列 topic:通配符 headers:参数匹配）
        //durable： 是否持久化   autoDelete： 自动删除    internal： 内部使用 一般为false  argument:参数
        String exchangeName="test_direct";
        channel.exchangeDeclare(exchangeName,BuiltinExchangeType.DIRECT,true,false,false,null);
        //6.创建两个队列
        String queue1Name="test_direct_queue1";
        String queue2Name="test_direct_queue2";
        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);

        //7.绑定队列和交换机
        //queue:绑定的队列名称 exchange:交换机名称  routingkey:路由键，绑定规则（如果交换机类型为fanout,routingKey设置为""）
        //队列一绑定error 队列二绑定info error warning
        channel.queueBind(queue1Name,exchangeName,"error");
        channel.queueBind(queue2Name,exchangeName,"info");
        channel.queueBind(queue2Name,exchangeName,"error");
        channel.queueBind(queue2Name,exchangeName,"warning");


        String body="日志级别：info..";
        //8.发送消息

        channel.basicPublish(exchangeName,"error",null,body.getBytes());



        //7.释放资源
        channel.close();
        connection.close();
    }
}
