package sssunday.rabbit.rabbitmq.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

public class Producer {

	public static final String EXCHANGE_NAME = "direct_exchange";
	public static final String ROUTING_KEY_ERROR = "direct_routingKey_error";
	public static final String ROUTING_KEY_INFO = "direct_routingKey_info";
	public static final String ROUTING_KEY_WARNING = "direct_routingKey_warning";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", false, false, null);
		String message_error = "hello world error";
		String message_info = "hello world info";
		String message_waring = "hello world waring";
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_ERROR, null, message_error.getBytes());
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_INFO, null, message_info.getBytes());
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_WARNING, null, message_waring.getBytes());
		channel.close();
		connection.close();
	}
}
