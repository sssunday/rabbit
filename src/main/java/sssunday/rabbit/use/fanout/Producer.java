package sssunday.rabbit.rabbitmq.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

public class Producer {

	public static final String EXCHANGE_NAME = "fanout_exchange";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout", false, false, null);
		for (int i = 0; i < 50; i++) {
			String message = "hello world " + i;
			System.out.println(message);
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());//跟routingKey无关
		}
		
		channel.close();
		connection.close();
		
		
	}
}
