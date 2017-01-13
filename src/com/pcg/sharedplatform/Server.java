package com.pcg.sharedplatform;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class ServerReadThread extends Thread {
	private Server server;
	private BufferedReader in;

	public ServerReadThread(Server server, BufferedReader in) {
		this.server = server;
		this.in = in;
	}

	public void run() {
		String message;
		while (true) {
			try {
				message = in.readLine();
				if (message == null) {
					System.out.println("Client disconnected");
					break;
				}
				System.out.println(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

public class Server extends Thread {
	private Controller controller;
	private ServerSocket servSock;
	private Map<String, Socket> ipToSocket;

	private BufferedReader in;
	private DataOutputStream out;

	public Server(Controller ctrl) {
		super();
		controller = ctrl;
		ipToSocket = new HashMap<>();
		servSock = null;
	}

	public void run() {
		while (true) {
			try {
				Socket clntSock = servSock.accept();
				int lastSize = ipToSocket.size();
				ipToSocket.put(clntSock.getInetAddress().getHostAddress(), clntSock);
				if (lastSize != ipToSocket.size()) {
					Platform.runLater(() -> controller.addWaitingClient(clntSock.getInetAddress().getHostAddress()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startServer(int port) throws IOException {
		servSock = new ServerSocket(port);
	}

	public static String getLocalHostIP() {
		String ip = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public void startClient(String ip) {
		try {
			out = new DataOutputStream(ipToSocket.get(ip).getOutputStream());
			in = new BufferedReader(new InputStreamReader(ipToSocket.get(ip).getInputStream(), "UTF-8"));
			ServerReadThread thread = new ServerReadThread(this, in);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeClient(String message) {
		try {
			System.out.println(message);
			out.flush();
			out.writeUTF(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopClient() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
