package sssunday.rabbit.use.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Producer {

	public static final String QUEUE_NAME = "simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String msg = "simple message";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		
		channel.close();
		connection.close();
	}
}
