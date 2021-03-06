package sssunday.rabbit.use.exbind;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Consumer {

	public static final String EXCHANGE_NAME_DESTINATION = "destination_exchange";
	public static final String QUEUE_NAME = "exbind_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = RabbitUtils.getConnection();
		final Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME_DESTINATION, "direct", false, false, null);
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME_DESTINATION, "routingKey1");
		channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println(new String(body, "UTF-8"));
				
				channel.basicAck(envelope.getDeliveryTag(), false);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("consumer1 done");
				}
			}
		});
		
	}
}
