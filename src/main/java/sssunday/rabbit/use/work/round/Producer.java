package sssunday.rabbit.use.work.round;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Producer {

	public static final String QUEUE_NAME = "work_queue_round";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		for (int i = 0; i < 200 ; i++) {
			String msg = "hello world " + i;
			System.out.println("send " + msg);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		channel.close();
		connection.close();
	}
}
