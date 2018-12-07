package sssunday.rabbit.rabbitmq.exbind;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

/**
 * 交换器绑定交换器
 * @author shuxue
 *
 */
public class Producer {

	public static final String EXCHANGE_NAME_SOURCE = "source_exchange";
	public static final String EXCHANGE_NAME_DESTINATION = "destination_exchange";
	private static final String ROUTING_KEY = "routingKey1";
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME_SOURCE, "direct", false, false, null);
		channel.exchangeDeclare(EXCHANGE_NAME_DESTINATION, "direct", false, false, null);
		channel.exchangeBind(EXCHANGE_NAME_DESTINATION, EXCHANGE_NAME_SOURCE, ROUTING_KEY);
		String message1 = "hello world routingKey1";
		channel.basicPublish(EXCHANGE_NAME_SOURCE, "routingKey1", null, message1.getBytes());
		
		String message2 = "hello world routingKey2";
		channel.basicPublish(EXCHANGE_NAME_SOURCE, "routingKey2", null, message2.getBytes());
		
		channel.close();
		connection.close();
		
		
	}
}
