package com.pcg.sharedplatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Task {
	public String instruction;
	public String tag;

	public Task(String instruction, String tag) {
		this.instruction = instruction;
		this.tag = tag;
	}
}

public class Tasks {
	public final static String startInstruction = "您好，感谢您参与本次实验！\n" +
			"您的参与将会帮助我们进一步提升移动触屏设备的用户体验。\n" +
			"请您填写您的个人相关信息：\n" +
			"\n" +
			"在实验中，请先阅读屏幕上的提示，准备好时请点击“开始当前任务”\n" +
			"完成当前任务后，点击“下一步”，跳转到下个任务页面\n" +
			"操作过程中有任何问题，都请向您身旁的工作人员提出，我们需要您的反馈\n" +
			"\n" +
			"现在就点击“下一步”，跳转到第一个任务页面，开始实验吧！";
	public final static String finishInstruction = "实验结束，感谢您的配合！";

	public int currentTask;
	private List<Task> taskList;

	public Tasks() {
		init();
	}

	public void init() {
		currentTask = -1;
		taskList = null;
	}

	public void next() {
		currentTask += 1;
	}

	public String getCurrentInstruction() {
		if ((currentTask < 0) || (currentTask >= taskList.size())) {
			return "";
		}
		return taskList.get(currentTask).instruction;
	}

	public String getCurrentTag() {
		if ((currentTask < 0) || (currentTask >= taskList.size())) {
			return "";
		}
		return taskList.get(currentTask).tag;
	}

	public boolean isLoaded() {
		if ((taskList == null) || (taskList.size() <= 0)) {
			return false;
		}
		return true;
	}

	public void loadTasks(File file) {
		currentTask = -1;
		taskList = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String message = scanner.nextLine();
				System.out.println("Message: " + message);
				String[] message_split = message.split("\\|");
				if (message_split.length >= 2) {
					Task task = new Task(message_split[0].replace('$', '\n'), message_split[1]);
					taskList.add(task);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isStarted() {
		if (currentTask < 0) {
			return false;
		}
		return true;
	}

	public boolean isFinished() {
		if (currentTask < taskList.size()) {
			return false;
		}
		return true;
	}
}
