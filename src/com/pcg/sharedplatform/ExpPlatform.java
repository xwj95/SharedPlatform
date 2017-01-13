package com.pcg.sharedplatform;

import java.io.IOException;

public class ExpPlatform {
	public static boolean isUnix = true;
	public static Controller controller;
	public static Collector collector;
	public static Server server;

	public String phoneType;
	public String userName;
	public Tasks tasks;
	public Sensor sensor;
	public Capacity capacity;
	public Touchevent touchevent;

	public ExpPlatform(Controller ctrl) {
		controller = ctrl;
		controller.setInstruction(Tasks.startInstruction);
		controller.setServerIP(Server.getLocalHostIP());
		if (System.getProperties().getProperty("os.name").startsWith("Windows")) {
			System.out.println("System: Windows");
			isUnix = false;
		}
		else {
			System.out.println("System: Unix");
			isUnix = true;
		}
		tasks = new Tasks();
		server = new Server(controller);
		collector = new Collector(this);
		sensor = new Sensor(this);
		capacity = new Capacity(this);
		touchevent = new Touchevent(this);
	}

	public boolean startServer(int port) {
		try {
			server.startServer(port);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean startExp() {
		if (!controller.isConnected()) {
			controller.messageBox("未选择设备！");
			return false;
		}
		if (!tasks.isLoaded()) {
			controller.messageBox("未选择实验！");
			return false;
		}
		phoneType = controller.getPhoneType();
		userName = controller.getUserName();
		if ((userName == null) || (userName.isEmpty())) {
			controller.messageBox("未输入用户名！");
			return false;
		}
		if (userName.indexOf(' ') >= 0) {
			controller.messageBox("用户名中不能包含空格！");
			return false;
		}
		server.startClient(controller.getSelectedClient());
		server.writeClient("name|" + userName);
		return true;
	}

	public boolean startTask() {
		sensor.startLog(userName, "task" + (tasks.currentTask + 1), tasks.getCurrentInstruction(), tasks.getCurrentTag());
		capacity.startLog(userName, "task" + (tasks.currentTask + 1));
		touchevent.startLog(userName, "task" + (tasks.currentTask + 1));
		return true;
	}

	public boolean stopTask() {
		if (tasks.isStarted()) {
			sensor.stopLog();
			capacity.stopLog();
			touchevent.stopLog();
		}
		return true;
	}

	public boolean nextTask() {
		tasks.next();
		if (tasks.isFinished()) {
			controller.setInstruction(Tasks.finishInstruction);
//			collector.collect(userName);
			tasks.init();
			return false;
		}
		controller.setInstruction(tasks.getCurrentInstruction());
		return true;
	}

	public boolean retryTask() {
		if (!stopTask()) {
			return false;
		};
		if (!startTask()) {
			return false;
		};
		return true;
	}
}
