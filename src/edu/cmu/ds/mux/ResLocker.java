package edu.cmu.ds.mux;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Message;
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
	private boolean isRequesting;
	//	private VOTE vote; // Is going to be deleted
	private Set<String> ackSet;
	private PriorityQueue<String> queue;

	public ResLocker(ConfigParser parser, String myName, ClockService uniClock, CriticalSection cs) throws FileNotFoundException, ParseException {
		super(parser, myName, uniClock);
		this.dedicatedGroup = parser.getMuxGroup(this.getMyName());
		this.cs = cs;
		this.isInCs = false;
		this.isRequesting = false;
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
		if (this.isRequesting) {
			MLogger.info("CS", "Already in Requesting");
			return false;
		}
		
		if (isInCs) {
			MLogger.info("CS", "Already in Critical Section");
			return false;
		}

		// String src, String group, String kind, String content
		GroupMessage gmsg = new GroupMessage(this.getMyName(), 
				this.dedicatedGroup,
				"", // What the kind is?
				"REQUEST");
		R_multicast(gmsg);
		this.isRequesting = true;

		return true;
		/* 
		 *  Wait until getting all the ACK.
		 *
		while (this.ackSet.size() < 3) {
			handleReceivedMessage();
		}
		this.cs.enterCs(this.getMyName());
		this.isInCs = true;
		return true;
		*/

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
	 * handleReceivedMessage - Handle the received requests sent by other processes.
	 */
	public void handleReceivedMessage() throws InterruptedException {
		GroupMessage gmsg = R_deliver();
//		MLogger.info("1", "ori = " + gmsg.getOrigin() + "\tsrc = " + gmsg.getSourceName() + "\ttarget = " + gmsg.getTargetName());
//		Message gmsg = receive();
		if (gmsg.getContent().equals("REQUEST")) {
			if (this.isInCs) {
				queue.add(gmsg.getSourceName());
			} else {
				sendAck(gmsg);
			}
		} else if (gmsg.getContent().equals("ACK")) {
			if (this.isRequesting) {
				this.ackSet.add(gmsg.getOrigin());
				///////// Test /////////
				MLogger.info("CS", "receive ACK from " + gmsg.getOrigin() + " ACK number: " + this.ackSet.size());
				///////// End Test /////////
				if (this.ackSet.size() == 3) {
					this.cs.enterCs(this.getMyName());
					this.isInCs = true;
					this.isRequesting = false;
				}
					
	//			this.ackSet.add(gmsg.getOrigin());
			}
		}

	}


	/**
	 *  Send ACK to all the members in the group by using R_multicast.
	 */
	public void sendAck(GroupMessage gmsg) {
		if (!isInCs) {
			
			// String src, String target, String kind, String content
			GroupMessage msg = new GroupMessage(this.getMyName(),
					gmsg.getGroup(),
					"", // What the kind is?
					"ACK");
			msg.setTargetName(gmsg.getOrigin());
//			System.out.println("1");
			
			send(msg);
		} 
	}

	/**
	 *  Use R_deliver to receive ACK.
	 */
	/*
	public void receiveAck() throws InterruptedException {
		GroupMessage gmssg = R_deliver();
		if (gmsg.getContent().equals("ACK")) {
			
			///////// Test /////////
			MLogger.info("CS", "receive ACK from " + gmsg.getSourceName());
			///////// End Test /////////
			
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
