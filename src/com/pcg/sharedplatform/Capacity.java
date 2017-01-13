package com.pcg.sharedplatform;

import javafx.application.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class CapacityDataThread_LongIsland implements Runnable {
	private ExpPlatform platform;
	private volatile Thread thread;
	private String dirName;
	private Process ps;

	public CapacityDataThread_LongIsland(ExpPlatform expPlatform) {
		platform = expPlatform;
		ps = null;
	}

	@Override
	public void run() {
		try {
			try {
				File path = new File("cmd/");
				if (!path.exists())
					path.mkdir();
				FileOutputStream fos = new FileOutputStream(new File("cmd/" + dirName + ".txt"));
				String strcmd = "cd sdcard\nsh capacity.sh " + dirName;
				fos.write(strcmd.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				ps = Runtime.getRuntime().exec("cmd /c adb shell < cmd\\" + dirName + ".txt");
				ps.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			System.out.println("CapacityDataThread - successful interrupt");
			return;
		}
		System.out.println("CapacityDataThread - unexcepted finish");
	}

	public void start(String dirName) {
		if (thread == null) {
			this.dirName = dirName;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void finish() {
		ps.destroy();
		thread.interrupt();
		thread = null;
		try {
			ps = Runtime.getRuntime().exec("cmd /c adb shell kill -9 `cat /sdcard/CapacityData/toBeKilled.pid`");
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
			if (platform.isUnix) {
				command = "adb shell sh sdcard/capacity_LittleV.sh " + userName + " " + taskName;
			}
			else {
				command = "cmd /c adb shell sh sdcard/capacity_LittleV.sh " + userName + " " + taskName;
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = true;
		ps.destroy();
		ps = null;
		System.out.println("CapacityDataThread_LittleV Start");
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
			if (platform.isUnix) {
				command = "adb shell set `ls -t data/log/dmd_log/ | grep '^logtofile'`; "
						+ "cp data/log/dmd_log/$1 sdcard/ExpData/" + userName + "/" + taskName + "; "
						+ "mv sdcard/ExpData/" + userName + "/" + taskName + "/$1 sdcard/ExpData/" + userName + "/" + taskName + "/capacity_" + taskName + ".thplog";
			}
			else {
				command = "cmd /c adb shell set `ls -t data/log/dmd_log/ | grep '^logtofile'``; "
						+ "cp data/log/dmd_log/$1 sdcard/ExpData/" + userName + "/" + taskName + "; "
						+ "mv sdcard/ExpData/" + userName + "/" + taskName + "/$1 sdcard/ExpData/" + userName + "/" + taskName + "/capacity_" + taskName + ".thplog";
			}
			ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
		ps.destroy();
		ps = null;
		System.out.println("CapacityDataThread_LittleV Finish");
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
			longIsland.start(userName);
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
