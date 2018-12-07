package sssunday.rabbit.use.trans;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Producer1 {

	public static final String QUEUE_NAME = "trans_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		try {
			channel.txSelect();
			String msg = "trans message1";
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
//			int a = 1/0;
			channel.txCommit();
		} catch (Exception e) {
			e.printStackTrace();
			channel.txRollback();
		}
		
		
		channel.close();
		connection.close();
	}
}
