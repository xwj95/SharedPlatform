package com.pcg.sharedplatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
			File path = new File("sh/cmd/");
			if (!path.exists()) {
				path.mkdir();
			}
			FileOutputStream fos = new FileOutputStream(new File("sh/cmd/" + userName + "_" + taskName + ".txt"));
			String strcmd = "sh sdcard/touch.sh " + userName + " " + taskName;
			fos.write(strcmd.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("ToucheventThread - successful interrupt");
			return;
		}
		System.out.println("ToucheventThread - unexpected finish");
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
