package edu.cmu.ds.logger;

import java.util.Map;
import java.util.PriorityQueue;

import edu.cmu.ds.clock.ITimestamp;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Host;
import edu.cmu.ds.message.Message;

public class Logger {
	
	PriorityQueue<TimeStampedMessage> queue = new PriorityQueue<TimeStampedMessage>();
	
	public Logger() {
	}
	
	public void addLog(TimeStampedMessage newMsg) {
		queue.add(newMsg);
	}
	
	public PriorityQueue<TimeStampedMessage> getLog() {
		return queue;
	}
	
	public void printLog() {
		PriorityQueue<TimeStampedMessage> temp = new PriorityQueue<TimeStampedMessage>(queue);
		
		while (!temp.isEmpty()) {
			TimeStampedMessage nextOne = temp.poll();
			ITimestamp nextTime = nextOne.getTimestamp();
			Message msg = nextOne.getMessage();
			System.out.println(nextTime.toString() + "[src: " + msg.getSourceName() + "; dest: " +
							   msg.getTargetName() + "] " + msg.getContent());
		}
	}
}
