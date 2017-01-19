package com.pcg.sharedplatform;

import java.io.*;

class CapacityDataThread_LongIsland implements Runnable {
	private ExpPlatform platform;
	private volatile Thread thread;
	private String userName;
	private String taskName;
	private Process ps;

	public CapacityDataThread_LongIsland(ExpPlatform expPlatform) {
		platform = expPlatform;
		ps = null;
	}

	@Override
	public void run() {
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell sh sdcard/capacity_LongIsland.sh " + userName + " " + taskName;
			}
			else {
				command = "cmd /c adb shell sh sdcard/capacity_LongIsland.sh " + userName + " " + taskName;
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("CapacityDataThread_LongIsland - successful interrupt");
			return;
		}
		System.out.println("CapacityDataThread_LongIsland - unexcepted finish");
	}

	public void start(String userName, String taskName) {
		if (thread == null) {
			this.userName = userName;
			this.taskName = taskName;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void finish() {
		ps.destroy();
		thread.interrupt();
		thread = null;
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell kill -9 `cat sdcard/ExpData/" + userName + "/" + taskName + "/capacity_" + taskName + "/toBeKilled.pid`";
			}
			else {
				command = "cmd /c adb shell kill -9 `cat sdcard/ExpData/" + userName + "/" + taskName + "/capacity_" + taskName + "/toBeKilled.pid`";
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ps.destroy();
		ps = null;
	}
}

class CapacityDataThread_LittleV {
	private ExpPlatform platform;
	private String userName;
	private String taskName;
	private boolean running;
	private Process ps;

	public CapacityDataThread_LittleV(ExpPlatform expPlatform) {
		platform = expPlatform;
		running = false;
		ps = null;
	}

	public void start(String userName, String taskName) {
		if (running) {
			System.out.println("Reject - CapacityDataThread_LittleV Still Running");
			return;
		}
		this.userName = userName;
		this.taskName = taskName;
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell aptouch_daemon_debug logtofile off";
			}
			else {
				command = "cmd /c adb shell aptouch_daemon_debug logtofile off";
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = input.readLine()) != null){
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell sh sdcard/capacity_LittleV.sh " + userName + " " + taskName;
			}
			else {
				command = "cmd /c adb shell sh sdcard/capacity_LittleV.sh " + userName + " " + taskName;
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
				if (line.startsWith("detect")) {
					platform.controller.messageBox("电容数据采集失败，\n请不要触摸手机，\n并重试当前任务！");
					platform.controller.disableNext();
				}
			}
			System.out.println("CapacityDataThread_LittleV Start");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = true;
		ps.destroy();
		ps = null;
	}

	public void finish() {
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell aptouch_daemon_debug logtofile off";
			}
			else {
				command = "cmd /c adb shell aptouch_daemon_debug logtofile off";
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("CapacityDataThread_LittleV Finish");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell set `ls -t data/log/dmd_log/ | grep '^logtofile'`; "
						+ "cp data/log/dmd_log/$1 sdcard/ExpData/" + userName + "/" + taskName + "; "
						+ "mv sdcard/ExpData/" + userName + "/" + taskName + "/$1 sdcard/ExpData/" + userName + "/" + taskName + "/capacity_" + taskName + ".thplog";
			}
			else {
				command = "cmd /c adb shell sh sdcard/copy_capacity.sh " + userName + " " + taskName;
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = input.readLine()) != null){
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
		ps.destroy();
		ps = null;
	}
}

public class Capacity {
	private ExpPlatform platform;
	private CapacityDataThread_LittleV littleV;
	private CapacityDataThread_LongIsland longIsland;

	public Capacity(ExpPlatform expPlatform) {
		platform = expPlatform;
	}

	public void startLog(String userName, String taskName) {
		if (platform.phoneType.equals("LITTLEV")) {
			littleV = new CapacityDataThread_LittleV(platform);
			littleV.start(userName, taskName);
		}
		else if (platform.phoneType.equals("LONGISLAND")) {
			longIsland = new CapacityDataThread_LongIsland(platform);
			longIsland.start(userName, taskName);
		}
	}

	public void stopLog() {
		if (platform.phoneType.equals("LITTLEV")) {
			littleV.finish();
		}
		else if (platform.phoneType.equals("LONGISLAND")) {
			longIsland.finish();
		}
	}
}
