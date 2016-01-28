package edu.cmu.dsmessage;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private Set<String> receiveDelayRules; // Update 01/25
    private Set<String> sendDelayRules;
    boolean isSendDelay; // Update 01/25
    boolean isReceiveDelay; // Update 01/26
    List<Message> sendDelayPool; // Update 01/26
    List<Message> receiveDelayPool; // Update 01/25

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
        this.receiveDelayRules 		= reader.getReceiveDelayRules(configFile); // Update 01/25
        
        for (String i : this.sendDelayRules) {
        	System.out.println("sendDelayRules: ");
        	System.out.print(i + "\t");
        }
        System.out.println();
        for (String i : this.receiveDelayRules) {
        	System.out.println("receiveDelayRules: ");
        	System.out.println(i + "\t");
        }
        
        /*
         *  Initialize the local variables.
         */
        this.isSendDelay = false; // Update 01/25
        this.isReceiveDelay = false; // Update 01/26
        this.sendDelayPool = new ArrayList<>(); // Update 01/26
        this.receiveDelayPool = new ArrayList<>(); // Update 01/25

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
    		if (this.sendDropRules.get(src) == null) {
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
    	if (this.sendDelayRules.contains(src)) {
    		System.out.println("--------- Send from " + src + " , Delay ---------");
    		this.sendDelayPool.add(msg);
    		this.isSendDelay = true;
    		return;
    	}
    	
    	this.controller.increaseSeqNum(dest); // Increase the seqNum of specified dest. 	
        this.controller.appendSendingMessage(msg);
        for (Message i : this.sendDelayPool) {
        	this.controller.appendSendingMessage(i);
        }
        this.sendDelayPool.clear();
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
    	if (this.isReceiveDelay == true) {
    		Message msg = this.receiveDelayPool.get(0);
    		this.receiveDelayPool.remove(0);
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
 //   		   this.receiveDropRules.get(src) == null ||
    		   this.receiveDropRules.get(src).contains(dest)) {
			System.out.println("--------- Drop at recieving, src = " + src + ", dest = " + dest + " ---------"); // For testing
		    msg = controller.takeReceivedMessage();
		    src = msg.getSourceName();
	    	dest = msg.getTargetName();
    	}
    	
    	while (this.receiveDelayRules.contains(src)) {
    		System.out.println("--------- Receive from " + src + ", Delay ---------");
    		this.receiveDelayPool.add(msg);
    		msg = controller.takeReceivedMessage();
    		src = msg.getSourceName();
    		dest = msg.getTargetName();
    		this.isReceiveDelay = true;
    	}
    	
        return msg;
    } 
    

}
