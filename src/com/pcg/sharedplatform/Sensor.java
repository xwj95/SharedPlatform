package com.pcg.sharedplatform;

public class Sensor {
	ExpPlatform platform;

	public Sensor(ExpPlatform expPlatform) {
		platform = expPlatform;
	}

	public void startLog(String userName, String taskName) {
		platform.server.writeClient("task|" + taskName);
	}

	public void stopLog() {
		platform.server.writeClient("end|end");
	}
}
