package sssunday.mavendemo.rabbitmq.work.fair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import sssunday.mavendemo.rabbitmq.utils.RabbitUtils;

public class Consumer1 {

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		final Channel channel = connection.createChannel();
		channel.queueDeclare(Producer.QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				System.out.println("consumer1 " + new String(body, "utf-8"));
				channel.basicAck(envelope.getDeliveryTag(), false);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("consumer1 done");
				}
			}
		};
		channel.basicConsume(Producer.QUEUE_NAME, false, defaultConsumer);
	}
}
