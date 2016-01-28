package edu.cmu.dsmessage;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by gavin on 1/19/16.
 */
public class MessagePasser {
    private Controller controller;
    private Map<String, Set<String>> sendDropRules;
    private Map<String, Integer> sendDropAfterRules;
    private Map<String, Set<String>> receiveDropRules;
    private Map<String, Integer> receiveDropAfterRules;
    private Map<String, Set<String>> receiveDelayRules; 
    private Map<String, Set<String>> sendDelayRules;
    boolean isReceiveDelay;
    Queue<Message> sendDelayPool; 
    Queue<Message> receiveDelayPool; 
    
    public MessagePasser(String configFile, String myName) throws ParseException, FileNotFoundException{
        ConfigParser parser = new ConfigParser(configFile);
        YamlReader reader = new YamlReader();
        
        /* 
         *  Initialize the global rules.
         */
        this.sendDropRules 			= reader.getSendDropRules(configFile);
        this.sendDropAfterRules 	= reader.getSendDropAfterRules(configFile);
        this.sendDelayRules 		= reader.getSendDelayRules(configFile);
        this.receiveDropRules 		= reader.getReceiveDropRules(configFile);
        this.receiveDropAfterRules 	= reader.getReceiveDropAfterRules(configFile);
        this.receiveDelayRules 		= reader.getReceiveDelayRules(configFile); 
        
        /*
         *  Initialize the local variables.
         */
        this.isReceiveDelay = false; 
        this.sendDelayPool = new LinkedList<>(); 
        this.receiveDelayPool = new LinkedList<>(); 

        this.controller = new Controller(parser, myName);

        Thread listener = new Thread(new Listener(controller));
        listener.start();

        Thread speaker = new Thread(new Speaker(controller));
        speaker.start();
    }

    /**
     * Add a message to the send queue. every thing in the queue will be sent.
     * @param msg the message to be sent
     */
    public void send(Message msg) {
        /*
        TODO: send messages according to the information stored in the Message object
        TODO: remember to check the rules before sending.
        */
    	String src = msg.getSourceName();
    	String dest = msg.getTargetName();
    	
    	System.out.println("SeqNum = " + this.controller.getSeqNum(dest));
    	
    	    	
    	if (this.sendDropRules.containsKey(src)) {
    		if (this.sendDropRules.get(src) == null) { // Wildcard
    			System.out.println("--------- Drop at sending ---------"); // For testing
    			return;    			
    		} else if (this.sendDropRules.get(src).contains(dest)) {
    			System.out.println("--------- Drop at sending ---------"); // For testing
    			return;
    		}
    	}
    	if (this.sendDropAfterRules.containsKey(src)) {
    		if (controller.getSeqNum(dest) > this.sendDropAfterRules.get(src)) {
    			System.out.println("--------- DropAfter at sending ---------");
    			return;
    		}
    	}
    	
    	if (this.sendDelayRules.containsKey(dest)) {
    		System.out.println("--------- Sent to " + dest + " , Delay ---------");
    		this.sendDelayPool.add(msg);
    		return;
    	}
    	
    	this.controller.increaseSeqNum(dest); // Increase the seqNum of specified dest. 	
        this.controller.appendSendingMessage(msg);
        
        /*
         *  If an non-delay message has been sent, check whether there is delay message in the pool.
         *  If so, send out all the delay message.
         */
        while (!this.sendDelayPool.isEmpty()) {
        	msg = this.sendDelayPool.poll();
        	this.controller.increaseSeqNum(dest);
        	this.controller.appendSendingMessage(msg);
        }
        
    }

    /**
     * poll out an element from the top of the queue.
     * @return List<Message>
     */
    public Message receive() throws InterruptedException{
    	
    	/*
    	 *  First check whether it is currently blocking, 
    	 *  if so, return the blocking message first.
    	 */
    	if (this.isReceiveDelay) {
    		Message msg = this.receiveDelayPool.poll();
    		if (this.receiveDelayPool.isEmpty()) {
    			this.isReceiveDelay = false;
    		}
    		return msg;
    	}
    	
    	
    	Message msg = controller.takeReceivedMessage();
    	String src = msg.getSourceName();
    	String dest = msg.getTargetName();
    	
    	/*
    	 *  Drop the message meets the rules until the one doesn't.
    	 */
    	while (this.receiveDropRules.containsKey(src) && 
    		   this.receiveDropRules.get(src).contains(dest)) {
			System.out.println("--------- Drop at recieving, src = " + src + ", dest = " + dest + " ---------"); // For testing
		    msg = controller.takeReceivedMessage();
		    src = msg.getSourceName();
	    	dest = msg.getTargetName();
    	}
    	
    	/*
    	 *  If the message is delay, block until there is a non-delay message coming.
    	 */
    	while (this.receiveDelayRules.containsKey(src)) {
    		if (this.receiveDelayRules.get(src) == null || this.receiveDelayRules.get(src).contains(dest)) {
	    		System.out.println("--------- Sent by " + src + ", Delay ---------");
	    		this.receiveDelayPool.add(msg);
	    		msg = controller.takeReceivedMessage();
	    		src = msg.getSourceName();
	    		dest = msg.getTargetName();
	    		this.isReceiveDelay = true;
    		}
    	}
    	
        return msg;
    } 
    

}
