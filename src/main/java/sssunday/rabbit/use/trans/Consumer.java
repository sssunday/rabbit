package sssunday.rabbit.rabbitmq.trans;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

public class Consumer {

	public static final String QUEUE_NAME = "trans_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		final Channel channel = connection.createChannel();
		channel.basicQos(1);
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				System.out.println(new String(body, "utf-8"));
				try {
					Thread.sleep(10);
					channel.basicAck(envelope.getDeliveryTag(), false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		channel.basicConsume(QUEUE_NAME, false, defaultConsumer);
	}
}
