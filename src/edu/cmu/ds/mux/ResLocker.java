package edu.cmu.ds.mux;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.util.MLogger;
import edu.cmu.ds.multicast.GroupMessage;
import edu.cmu.ds.multicast.MulticastMessagePasser;

/**
 * ResLocker - implemented with Maekawa's Algorithm
 *
 */
public class ResLocker extends MulticastMessagePasser implements ILocker {
	public static enum VOTE {
		UNVOTED, YES, NO;
	}

	private String dedicatedGroup;
	private CriticalSection cs;
	private boolean isInCs;
	//	private VOTE vote; // Is going to be deleted
	private Set<String> ackSet;
	private PriorityQueue<String> queue;

	public ResLocker(ConfigParser parser, String myName, ClockService uniClock, CriticalSection cs) throws FileNotFoundException, ParseException {
		super(parser, myName, uniClock);
		this.dedicatedGroup = parser.getMuxGroup(this.getMyName());
		this.cs = cs;
		this.isInCs = false;
		//    	this.vote = VOTE.UNVOTED;
		this.ackSet = new HashSet<String>();
		this.queue = new PriorityQueue<String>();
	}

	/**
	 * request - request the resource. Block until the process get the resource
	 * @return true when request success. false if the process is already in the Critical Section.
	 * @throws InterruptedException 
	 */
	@Override
	public boolean request() throws InterruptedException {
		if (isInCs) {
			return false;
		}

		// String src, String group, String kind, String content
		GroupMessage gmsg = new GroupMessage(this.getMyName(), 
				this.dedicatedGroup,
				"", // What the kind is?
				"REQUEST");
		R_multicast(gmsg);

		/* 
		 *  Wait until getting all the ACK.
		 */
		while (this.ackSet.size() < 3) {
			receiveAck();
		}
		this.cs.enterCs(this.getMyName());
		this.isInCs = true;
		return true;

	}

	/**
	 * release - release the resource.
	 * @return false when not in CS; true when release successfully.
	 */
	@Override
	public boolean release() {
		if (!this.isInCs) {
			return false;
		}
		this.isInCs = false;
		this.ackSet.clear();
		this.cs.leaveCs();
		return true;
	}

	/**
	 * handleRequest - Handle the received requests sent by other processes.
	 */
	public void handleRequest() throws InterruptedException {
		/* TODO: Handle the following:
		 * 		 1. Enqueue the process when there is a process in CS.
		 * 		 2. Tell process to send ACK.
		 */
		GroupMessage gmsg = R_deliver();
		if (!gmsg.getContent().equals("REQUEST")) {
			return;
		}

		if (this.isInCs) {
			queue.add(gmsg.getSourceName());
		} else {
			sendAck();
		}
	}


	/**
	 *  Send ACK to all the members in the group by using R_multicast.
	 */
	public void sendAck() {
		if (!isInCs) {
			// String src, String group, String kind, String content
			GroupMessage gmsg = new GroupMessage(this.getMyName(), 
					this.dedicatedGroup,
					"", // What the kind is?
					"ACK");
			R_multicast(gmsg);
		} 
	}

	/**
	 *  Use R_deliver to receive ACK.
	 */
	public void receiveAck() throws InterruptedException {
		GroupMessage gmsg = R_deliver();
		if (gmsg.getContent().equals("ACK")) {
			this.ackSet.add(gmsg.getOrigin());
		}
	}

	/**
	 * Info - show information
	 *  1. how many messages have been sent or received
	 *  2. whether in CS
	 */
	@Override
	public void info() {
		if (cs.getIsInCs()) {
			MLogger.info("Critical Section", cs.getProcessName() + " is currently in Critical Section");
		} else {
			
		}
	}
}
