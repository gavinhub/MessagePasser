package edu.cmu.ds.mux;

import java.util.PriorityQueue;

public class CriticalSection {
	private String currentProcess; // Used to indicate which process are currently in CS.
	private boolean isInCS; // Used to indicate that some process is in CS.
	private PriorityQueue<String> queue; // Used to store the process waiting for the CS.
	
	public CriticalSection() {
		this.isInCS = false;
		queue = new PriorityQueue<String>();
		
	}
	
	public void handleRequest(ResLocker locker) {
		/* TODO: Handle the following:
		 * 		 1. Enqueue the process when there is a process in CS.
		 * 		 2. Tell process to send ACK.
		 */
		
		if (this.isInCS) {
			queue.add(locker.getMyName());
		} else {
			locker.sendAck();
		}
	}
	
	public void enterCs(String name) {
		this.isInCS = true;
		currentProcess = name;
	}
	
	public void leaveCs() {
		this.isInCS = false;
	}
	
	public String getProcessName() {
		return this.currentProcess;
	}
}
