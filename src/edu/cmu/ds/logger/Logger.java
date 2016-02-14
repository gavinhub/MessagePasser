package edu.cmu.ds.logger;

import java.sql.Time;
import java.util.PriorityQueue;

import edu.cmu.ds.application.DSApplication;
import edu.cmu.ds.message.Message;
import edu.cmu.ds.message.util.MLogger;

public class Logger implements DSApplication {
	
	PriorityQueue<TimeStampedMessage> queue;
	
	public Logger() {
		queue = new PriorityQueue<>();
	}

	public void onMessage(Message msg) {
        MLogger.info("Logger", "new message");

		this.addLog(msg);
	}
	
	protected void addLog(Message newMsg) {
        TimeStampedMessage tsm = new TimeStampedMessage(newMsg.getTimestamp(), newMsg);
		queue.add(tsm);
	}
	
	public PriorityQueue<TimeStampedMessage> getLog() {
		return queue;
	}
	
	public void printLog() {
		PriorityQueue<TimeStampedMessage> temp = new PriorityQueue<>(queue);
		
		TimeStampedMessage lastMsg = null;
		int num = 0;
		boolean flag = false;
		while (!temp.isEmpty()) {
			TimeStampedMessage currentMsg = temp.poll();
			if (lastMsg != null && lastMsg.compareTo(currentMsg) == 0) {
				System.out.println("*Concurrent Message, num = " + num + "*" + currentMsg.toString());
				flag = true;
			} else {
				System.out.println(currentMsg.toString());
				if (flag) {
					num++;
					flag = false;
				}
			}
			lastMsg = currentMsg;
		}
	}
}
