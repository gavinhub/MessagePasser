package edu.cmu.dsmessage;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MessagePasser {
    private Controller controller;
	private List<Rule> sendRules;
	private List<Rule> receiveRules;
    private Queue<Message> sendDelayPool;
    private Queue<Message> receiveDelayPool;

    
    public MessagePasser(String configFile, String myName) throws ParseException, FileNotFoundException{
        ConfigParser parser = new ConfigParser(configFile);
        YamlReader reader = new YamlReader();
        
		this.sendRules = reader.getRules(configFile, "sendRules");
		this.receiveRules = reader.getRules(configFile, "receiveRules");
        
        /*
         *  Initialize the local variables.
         */
        this.sendDelayPool = new LinkedList<>(); 
        this.receiveDelayPool = new LinkedList<>();

        this.controller = new Controller(parser, myName);

		/* Start the threads */
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

    	String dest = msg.getTargetName();

		if (this.controller.lookUpHost(dest) == null) {
			System.out.println("No host found");
			return;
		}
		int seqNum = this.controller.getSeqNum(dest);
		this.controller.increaseSeqNum(dest);
    	msg.setSequenceNumber(seqNum); // need to set seqNum because it's not decided until sent.

		boolean matches = false;
		for (Rule rule: this.sendRules) {
			if (rule.match(msg)) {

				// if delay, add it to delay pool
				if (rule.action == Rule.Action.DELAY) {
					this.sendDelayPool.add(msg);
				}

				// whatever match, message is not sent
				matches = true;
				break;
			}
		}

		// successfully send a message
		// append all massages in the delay pool to sending queue
		if (!matches) {
			this.controller.appendSendingMessage(msg);
			while (!this.sendDelayPool.isEmpty()) {
				controller.appendSendingMessage(this.sendDelayPool.poll());
			}
		}
        
    }

    /**
     * poll out an element from the top of the queue.
     * @return Message
     */
    public Message receive() throws InterruptedException{

        if (!this.receiveDelayPool.isEmpty()) {
            return this.receiveDelayPool.poll();
        }

    	/* Delay pool is empty, check new messages and add them to delay pool
    	 * until first non-delay message comes out.
    	 */
        while (true) {
            Message msg = controller.takeReceivedMessage();
            boolean matches = false;
            for (Rule rule: this.receiveRules) {
                if (rule.match(msg)) {
                    if (rule.action == Rule.Action.DELAY) {
                        this.receiveDelayPool.add(msg);
                    }
                    matches = true;
                    break;
                }
            }

            if (!matches) {
                return msg;
            }
        }
    } 
    

}
