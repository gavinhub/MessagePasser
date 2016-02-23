package edu.cmu.ds.mux;

import java.util.PriorityQueue;

import edu.cmu.ds.message.util.MLogger;

public class CriticalSection {
	private String currentProcess; // Used to indicate which process are currently in CS.
	private static Boolean isInCs; // Used to indicate that some process is in CS.
	
	public CriticalSection() {
		CriticalSection.isInCs = false;
	}
	
	protected void enterCs(String name) {
		CriticalSection.isInCs = true;
		MLogger.message("Critical Section", name + " is currently in the Critical Section");
		currentProcess = name;
	}
	
	protected void leaveCs() {
		MLogger.message("Critical Section", this.currentProcess + " has already left the Critical Section");
		CriticalSection.isInCs = false;
		this.currentProcess = null;
	}
	
	protected String getProcessName() {
		return this.currentProcess;
	}
	
	public boolean getIsInCs() {
		return CriticalSection.isInCs;
	}
}
