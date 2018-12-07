package sssunday.rabbit.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

public class Consumer1 {

	public static final String EXCHANGE_NAME = "topic_exchange";
	public static final String QUEUE_NAME = "topic_queue1";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		final Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic", false, false, null);
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "user.#");
		channel.basicQos(1);
		channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println(new String(body, "UTF-8"));
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		});
	}
	
}
