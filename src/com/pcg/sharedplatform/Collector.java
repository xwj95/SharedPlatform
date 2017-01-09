package com.pcg.sharedplatform;

import java.io.IOException;

public class Collector {
	private ExpPlatform platform;

	public Collector(ExpPlatform expPlatform) {
		platform = expPlatform;
	}

	public void collect(String userName) {
		try {
			String command;
			if (platform.isUnix) {
				command = "sh sh/touch.sh " + userName;
			} else {
				command = "cmd /c sh/touch.bat " + userName;
			}
			Process ps = Runtime.getRuntime().exec(command);
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
