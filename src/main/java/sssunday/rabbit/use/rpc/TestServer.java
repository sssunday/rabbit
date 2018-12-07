package sssunday.rabbit.use.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import sssunday.rabbit.use.utils.RabbitUtils;

public class TestServer {

public static final String QUEUE_NAME = "rpc_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				BasicProperties replyProp = new BasicProperties.Builder()
						.correlationId(properties.getCorrelationId())
						.build();
				String message = new String(body, "UTF-8");
				System.out.println(message);
				String result = dealMsg(message);
				channel.basicPublish("", properties.getReplyTo(), replyProp, result.getBytes());
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		});
	}

	protected static String dealMsg(String message) {
		String result = message + " reply";
		System.out.println("处理数据结果:" + result);
		return result;
	}
}
