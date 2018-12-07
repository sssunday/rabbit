package sssunday.rabbit.use.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Producer {

	public static final String EXCHANGE_NAME = "topic_exchange";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic", false, false, null);
		String message1 = "user.error msg";
		String message2 = "user.info msg";
		String message3 = "order.error msg";
		String message4 = "order.info msg";
		for (int i = 0; i < 10; i++) {
			channel.basicPublish(EXCHANGE_NAME, "user.error", null, (message1+i).getBytes());
			channel.basicPublish(EXCHANGE_NAME, "user.info", null, (message2+i).getBytes());
//			BasicProperties prop = new BasicProperties.Builder().contentType("text/plain").build();
//			channel.basicPublish(EXCHANGE_NAME, "user.info", true, true, prop, (message2+i).getBytes());
			channel.basicPublish(EXCHANGE_NAME, "order.error", null, (message3+i).getBytes());
			channel.basicPublish(EXCHANGE_NAME, "order.info", null, (message4+i).getBytes());
		}
		
		channel.close();
		connection.close();
	}
}
