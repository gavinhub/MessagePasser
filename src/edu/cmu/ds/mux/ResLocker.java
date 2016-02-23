package edu.cmu.ds.mux;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Message;
import edu.cmu.ds.message.util.MLogger;
import edu.cmu.ds.multicast.Group;
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
	private List<String> dedicatedGroupMembers;
	private CriticalSection cs;
	private boolean isInCs;
	private boolean isRequesting;
	private boolean isVoted; // Is going to be deleted
	private Set<String> ackSet;
	private Queue<GroupMessage> queue;

	public ResLocker(ConfigParser parser, String myName, ClockService uniClock, CriticalSection cs) throws FileNotFoundException, ParseException {
		super(parser, myName, uniClock);
		this.dedicatedGroup = parser.getMuxGroup(this.getMyName());
		this.cs = cs;
		this.isInCs = false;
		this.isRequesting = false;
		dedicatedGroupMembers = parser.getMuxGroupMembers(myName);
		MLogger.message("Group", "dedicatedGroup = " + this.dedicatedGroup + ";\tMembers = " + dedicatedGroupMembers);
				
		this.isVoted = false;
		this.ackSet = new HashSet<String>();
		this.queue = new LinkedList<GroupMessage>();
	}

	/**
	 * request - request the resource. No Block.
	 * @return true when request success. false if the process is already in the Critical Section.
	 * @throws InterruptedException 
	 */
	@Override
	public boolean request() throws InterruptedException {
		if (this.isRequesting) {
			MLogger.info("Locker", "Already in Requesting");
			return false;
		}

		// String src, String group, String kind, String content
		GroupMessage gmsg = new GroupMessage(this.getMyName(), 
				this.dedicatedGroup,
				"",
				"REQUEST");
		
		if (isInCs || cs.getIsInCs()) {
			MLogger.info("Locker", "Enque");
			queue.add(gmsg);
			return false;
		}

		R_multicast(gmsg);
		this.isRequesting = true;

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
//		this.cs.leaveCs();
		
		sendNotification("RELEASE");

		return true;
	}

	/**
	 * handleReceivedMessage - Handle the received requests sent by other processes.
	 */
	public void handleReceivedMessage() throws InterruptedException {
		GroupMessage gmsg = R_deliver();
		if (gmsg.getContent().equals("REQUEST")) {
			if (isVoted || this.isInCs) {

				queue.add(gmsg);
			} else {
				sendAck(gmsg);
				isVoted = true;
			}
		} else if (gmsg.getContent().equals("ACK")) {
			if (this.isRequesting) {
				if (dedicatedGroupMembers.contains(gmsg.getOrigin())) {
					this.ackSet.add(gmsg.getOrigin());
					///////// Test /////////
					MLogger.info("Locker", "receive ACK from " + gmsg.getOrigin() + "; ACK number: " + this.ackSet.size());
					///////// End Test /////////
					if (this.ackSet.size() == 3 && !this.isInCs) {
						this.isInCs = true;
						this.isRequesting = false;
						sendNotification("CS");
					}
				}
			}
		} else if (gmsg.getContent().equals("CS")) {
			cs.enterCs(gmsg.getOrigin());
		} else if (gmsg.getContent().equals("RELEASE")) {
			cs.leaveCs();
			if (!queue.isEmpty()) {
				GroupMessage head = queue.poll();
				sendAck(head);
				this.isRequesting = true;
				isVoted = true;
			} else {
				isVoted = false;
			}
		}
	}

	/**
	 *  Send ACK to all the members in the group by using R_multicast.
	 */
	public void sendAck(GroupMessage gmsg) {
		GroupMessage msg = new GroupMessage(this.getMyName(),
				gmsg.getGroup(),
				"", 
				"ACK");
		msg.setTargetName(gmsg.getOrigin());
		MLogger.info("ACK", "send ACK from " + msg.getOrigin() + " to " + msg.getTargetName());

		send(msg);

	}
	
	/**
	 *  Send notification (process gets in critical section or leave critical section)
	 *  to all the members in the group by using R_multicast.
	 */
	protected void sendNotification(String notification) {
		GroupMessage gmsg = new GroupMessage(this.getMyName(), 
				this.dedicatedGroup,
				"", 
				notification);
		R_multicast(gmsg);
	}
	

	/**
	 * Info - show information
	 *  1. how many messages have been sent or received
	 *  2. whether in CS
	 */
	@Override
	public void info() {
		if (cs.getIsInCs()) {
			MLogger.info("Locker", cs.getProcessName() + " is currently in Critical Section");
		} else {
			MLogger.info("Locker", "I am not in CS");
		}
	}
}
