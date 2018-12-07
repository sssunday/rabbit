package sssunday.mavendemo.rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import sssunday.mavendemo.rabbitmq.utils.RabbitUtils;

public class Consumer {

	public static final String QUEUE_NAME = "simple_queue_slave1";
	public static final String EXCHANGE_NAME = "simple_exchange_slave1";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", false, false, null);
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
//			@Override
//			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
//					throws IOException {
//				
//				System.out.println(new String(body, "utf-8"));
//				
//			}
//		};
//		channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
	}
}
