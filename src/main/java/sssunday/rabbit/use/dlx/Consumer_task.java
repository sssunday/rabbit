package sssunday.rabbit.use.dlx;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Consumer_task {

	public static final String TASK_QUEUE_NAME = "task_queue";
	public static final String ROUTING_KEY = "task_routingKey";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		final Channel channel = connection.createChannel();
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				System.out.println(new String(body, "utf-8"));
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		channel.basicConsume(TASK_QUEUE_NAME, false, defaultConsumer);
	}
}
