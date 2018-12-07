package sssunday.rabbit.rabbitmq.dlx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.rabbitmq.utils.RabbitUtils;
/**
 * 死信交换器
 * @author shuxue
 *
 */
public class Producer {

	public static final String DELAY_EXCHANGE_NAME = "delay_exchange";
	public static final String DELAY_QUEUE_NAME = "delay_queue";
	public static final String DELAY_ROUTING_KEY = "delay_routingKey";
	
	public static final String TASK_EXCHANGE_NAME = "task_exchange";
	public static final String TASK_QUEUE_NAME = "task_queue";
	public static final String TASK_ROUTING_KEY = "task_routingKey";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		
		//死信交换器和死信队列的声明
		channel.exchangeDeclare(TASK_EXCHANGE_NAME, "direct", false, false, null);
		channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
		channel.queueBind(TASK_QUEUE_NAME, TASK_EXCHANGE_NAME, TASK_ROUTING_KEY);
		//配置死信参数
		Map<String, Object> argumentsDelay = new HashMap<String, Object>();
//		argumentsDelay.put("x-max-length", 10);
		argumentsDelay.put("x-dead-letter-exchange", TASK_EXCHANGE_NAME);
		argumentsDelay.put("x-dead-letter-routing-key", TASK_ROUTING_KEY);
		//延迟队列声明，并绑定死信交换器
		channel.exchangeDeclare(DELAY_EXCHANGE_NAME, "direct", false, false, null);
		channel.queueDeclare(DELAY_QUEUE_NAME, false, false, false, argumentsDelay);
		channel.queueBind(DELAY_QUEUE_NAME, DELAY_EXCHANGE_NAME, DELAY_ROUTING_KEY);
		
		
		String msg = "simple message";
		BasicProperties prop = new BasicProperties.Builder().expiration("10000").build();
		for (int i = 0; i < 20; i++) {
			Thread.sleep(2000);
			msg = msg + i;
			channel.basicPublish(DELAY_EXCHANGE_NAME, DELAY_ROUTING_KEY, prop, msg.getBytes());
		}
		channel.close();
		connection.close();
	}
}
