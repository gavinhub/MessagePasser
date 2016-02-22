package edu.cmu.ds.mux;

import java.util.PriorityQueue;

import edu.cmu.ds.message.util.MLogger;

public class CriticalSection {
	private String currentProcess; // Used to indicate which process are currently in CS.
	private boolean isInCs; // Used to indicate that some process is in CS.
	
	public CriticalSection() {
		this.isInCs = false;
	}
	
	protected void enterCs(String name) {
		this.isInCs = true;
		MLogger.info("Critical Section", name + " is currently in the Critical Section");
		currentProcess = name;
	}
	
	protected void leaveCs() {
		this.isInCs = false;
		MLogger.info("Critical Section", this.currentProcess + " is curretnly leaving the Critical Section");
		this.currentProcess = null;
	}
	
	protected String getProcessName() {
		return this.currentProcess;
	}
	
	public boolean getIsInCs() {
		return this.isInCs;
	}
}
