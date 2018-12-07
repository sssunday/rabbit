package sssunday.mavendemo.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class sockClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1",12345);
		OutputStream outputStream = socket.getOutputStream();
		String message = "hello world";
		System.out.println(message + " sending...");
		outputStream.write(message.getBytes());
		System.out.println(message + " sended");
		socket.close();
	}
}
