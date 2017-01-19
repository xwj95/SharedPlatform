package com.pcg.sharedplatform;

import java.io.*;

public class Touchevent implements Runnable {
	private ExpPlatform platform;
	private volatile Thread thread;
	private String userName;
	private String taskName;
	private Process ps;
	private Process kill_ps;

	public Touchevent(ExpPlatform expPlatform) {
		platform = expPlatform;
		ps = null;
		kill_ps = null;
	}

	@Override
	public void run() {
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell sh sdcard/touch.sh " + userName + " " + taskName;
			}
			else {
				command = "cmd /c adb shell sh sdcard/touch.sh " + userName + " " + taskName;
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
			System.out.println("ToucheventThread - successful interrupt");
			return;
		}
		System.out.println("ToucheventThread - unexpected finish");
		platform.controller.messageBox("adb连接断开！\n请重试当前任务！");
		platform.controller.disableNext();
	}

	public void startLog(String userName, String taskName) {
		if (thread == null) {
			this.userName = userName;
			this.taskName = taskName;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stopLog() {
		try {
			String command;
			if (platform.isUnix) {
				command = "adb shell \"set `ps | grep getevent`; kill -9 $2\"";
			}
			else {
				command = "cmd /c adb shell \"set `ps | grep getevent`; kill -9 $2\"";
			}
			kill_ps = Runtime.getRuntime().exec(command);
			kill_ps.waitFor();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(kill_ps.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt();
		thread = null;
		ps.destroy();
		ps = null;
		kill_ps.destroy();
		kill_ps = null;
	}
}
