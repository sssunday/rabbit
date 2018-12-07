package sssunday.rabbit.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SockServer {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(12345);
		Socket socket = ss.accept();
		InputStream inputStream = socket.getInputStream();
		byte [] bytes = new byte[1024] ;
		int len = inputStream.read(bytes);
		String data = new String(bytes,0,len);
		System.out.println(data + " received");
		ss.close();
	}
}
