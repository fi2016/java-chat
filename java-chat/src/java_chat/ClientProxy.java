package java_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProxy implements Runnable {
	
	private Socket socket;
	private Server server;
	private String nickname;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread t;

	public ClientProxy(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (!t.isInterrupted()) {
			try {
				if (in == null) {
					in = new ObjectInputStream(socket.getInputStream());
				}
				String request = in.readUTF();
				
				//only for debugging
				System.out.println(request);
			} catch (IOException e) {
				System.out.println("Failed reading request!");
				e.printStackTrace();
			}
		}
	}

	protected void sendMessage(String message) {
		try {
			if (out == null) {
				out = new ObjectOutputStream(socket.getOutputStream());
			}
			out.writeUTF(message);
		} catch (IOException e) {
			System.out.println("Failed sending response!");
			e.printStackTrace();
		}
	}

	protected void closeClient() {
		try {
			t.interrupt();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Failed closing client!");
			e.printStackTrace();
		}
	}
}