package edu.cmu.dsmessage;

import java.io.FileNotFoundException;
import java.text.ParseException;
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
    private Integer sequence;

    public MessagePasser(String configFile, String myName) throws ParseException, FileNotFoundException{
        ConfigParser parser = new ConfigParser(configFile);
        YamlReader reader = new YamlReader();
        this.sendDropRules = reader.getSendDropRules(configFile);
        this.sendDropAfterRules = reader.getSendDropAfterRules(configFile);
        this.receiveDropRules = reader.getReceiveDropRules(configFile);
        
        this.sequence = 0;

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
    	increaseSeqNum();
    	System.out.println("SeqNum = " + this.sequence);
    	String src = msg.getSourceName();
    	String dest = msg.getTargetName();
    	if (this.sendDropRules.containsKey(src)) {
    		if (this.sendDropRules.get(src).contains(dest)) {
    			System.out.println("--------- Drop at sending ---------"); // For testing
    			return;
    		}
    	}
    	if (this.sendDropAfterRules.containsKey(src)) {
    		if (this.sequence > this.sendDropAfterRules.get(src)) {
    			System.out.println("--------- DropAfter at sending ---------");
    			return;
    		}
    	}
        this.controller.appendSendingMessage(msg);
    }

    /**
     * poll out an element from the top of the queue.
     * @return Message
     */
    public Message receive() throws InterruptedException{
        // TODO: poll messages from the queue. You should check against the rolls. If dropped, poll next one.
    	System.out.println("SeqNum = " + this.sequence);
    	Message msg = controller.takeReceivedMessage();
    	String src = msg.getSourceName();
    	String dest = msg.getTargetName();
    	if (this.receiveDropRules.containsKey(src)) {
    		if (this.sendDropRules.get(src).contains(dest)) {
    			System.out.println("--------- Drop at recieving ---------"); // For testing
    			return null;
    		}
    	}
        return msg;
    }
    
    private void increaseSeqNum() {
    	this.sequence++;
    }

}
