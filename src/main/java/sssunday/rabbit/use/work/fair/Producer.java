package sssunday.rabbit.rabbitmq.work.fair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

public class Producer {

	public static final String QUEUE_NAME = "work_queue_fair";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);//无效
		for (int i = 0; i < 20 ; i++) {
			String msg = "hello world " + i;
			System.out.println("send " + msg);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		channel.close();
		connection.close();
	}
}
