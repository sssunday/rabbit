package sssunday.rabbit.rabbitmq.rpc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.RpcClient.Response;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;

public class TestClient {

	public static final String QUEUE_NAME = "rpc_queue";
	public static final String REPLY_QUEUE = "reply_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		final String uuid = UUID.randomUUID().toString();
		RpcClient client = new RpcClient(channel, "", QUEUE_NAME);
		String message = "10";
		BasicProperties basicProp = new BasicProperties.Builder().correlationId(uuid).replyTo(REPLY_QUEUE).build();
		Response response = client.doCall(basicProp, message.getBytes());
		System.out.println(new String(response.getBody()));
		channel.close();
		connection.close();
	}
}
