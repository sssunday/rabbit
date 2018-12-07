package sssunday.rabbit.use.trans;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import sssunday.rabbit.use.utils.RabbitUtils;

public class Producer2 {

	public static final String QUEUE_NAME = "trans_queue";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = RabbitUtils.getConnection();
		Channel channel = connection.createChannel();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("x-max-length", 8);
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.confirmSelect();
		
		//async
		final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		channel.addConfirmListener(new ConfirmListener() {
			//faild
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("nack - deliveryTag:" + deliveryTag + " - multiple:" + multiple);
				if(multiple){
					System.out.println("nack multi tag:" + deliveryTag + " - Set:" + confirmSet.headSet(deliveryTag + 1)); //headSet(E) 返回小于E的集合
					confirmSet.headSet(deliveryTag + 1).clear();
				} else {
					System.out.println("ack single tag:" + deliveryTag + " - Set:" + deliveryTag);
					confirmSet.remove(deliveryTag);
				}
			}
			//success
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("ack - deliveryTag:" + deliveryTag + " - multiple:" + multiple);
				if(multiple){
					System.out.println("ack multi tag:" + deliveryTag + " - Set:" + confirmSet.headSet(deliveryTag + 1)); //headSet(E) 返回小于E的集合
					confirmSet.headSet(deliveryTag + 1).clear();
				} else {
					System.out.println("ack single tag:" + deliveryTag + " - Set:" + deliveryTag);
					confirmSet.remove(deliveryTag);
				}
			}
		});
		
		for (int i = 0; i < 50; i++) {
			String msg = "trans message" + i;
			long nextSeqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			confirmSet.add(nextSeqNo);
		}
		
		//sync
		if(channel.waitForConfirms()){
			System.out.println("send successed");
		} else {
			System.out.println("send failed");
		}
		
		channel.close();
		connection.close();
	}
}
