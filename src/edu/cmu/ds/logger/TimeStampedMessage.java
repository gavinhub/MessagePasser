package edu.cmu.ds.logger;

import edu.cmu.ds.clock.ITimestamp;
import edu.cmu.ds.message.Message;

public class TimeStampedMessage implements Comparable<TimeStampedMessage> {

	ITimestamp timestamp;
	Message msg;
	
	public TimeStampedMessage(ITimestamp timestamp, Message msg) {
		this.timestamp = timestamp;
		this.msg = msg;
	}

	public ITimestamp getTimestamp() {
		return this.timestamp;
	}
	
	public Message getMessage() {
		return this.msg;
	}
	
	@Override
	public int compareTo(TimeStampedMessage o) {
		return this.timestamp.compareTo(o.getTimestamp());
	}

	public String toString() {
        return timestamp.toString() + "[src: " + msg.getSourceName() + "; dest: " +
                msg.getTargetName() + "] " + msg.getContent();
    }
}
