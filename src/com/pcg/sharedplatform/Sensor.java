package com.pcg.sharedplatform;

public class Sensor {
	ExpPlatform platform;

	public Sensor(ExpPlatform expPlatform) {
		platform = expPlatform;
	}

	public void startLog(String userName, String taskName, String instruction, String tag) {
		String message = "task|" + taskName;
//		String message = "task|" + taskName + "#" + instruction.replace('\n', '$') + "#" + tag.replace('\n', '$');
		platform.server.writeClient(message);
	}

	public void stopLog() {
		platform.server.writeClient("end|end");
	}
}
