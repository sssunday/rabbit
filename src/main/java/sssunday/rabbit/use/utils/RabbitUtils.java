package sssunday.rabbit.rabbitmq.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitUtils {

	public static Connection getConnection() throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("106.12.125.239");
		factory.setPort(5672);
		factory.setUsername("test");
		factory.setPassword("test");
		factory.setVirtualHost("vhost");
		return factory.newConnection();
	}
}
