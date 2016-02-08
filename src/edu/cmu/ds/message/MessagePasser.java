package edu.cmu.ds.message;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.logger.TimeStampedMessage;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessagePasser {
    private Controller controller;
	private List<Rule> sendRules;
	private List<Rule> receiveRules;
    private Queue<Message> sendDelayPool;
    private Queue<Message> receiveDelayPool;
    private ClockService clock;

    
    public MessagePasser(ConfigParser parser, String myName, ClockService clock) throws ParseException, FileNotFoundException{
        YamlReader reader = new YamlReader();

		this.sendRules = reader.getRules(parser.getFileName(), "sendRules");
        this.receiveRules = reader.getRules(parser.getFileName(), "receiveRules");
        
        /*
         *  Initialize the local variables.
         */
        this.sendDelayPool = new LinkedList<>(); 
        this.receiveDelayPool = new LinkedList<>();

        this.controller = new Controller(parser.getHosts(), myName);
        this.clock = clock;

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
            msg.setTimestamp(this.clock.next());
			this.controller.appendSendingMessage(msg);

			while (!this.sendDelayPool.isEmpty()) {
                Message nmsg = this.sendDelayPool.poll();
                msg.setTimestamp(this.clock.next());
				controller.appendSendingMessage(nmsg);
			}
		}
        
    }

    /**
     * poll out an element from the top of the queue.
     * @return Message
     */
    public Message receive() throws InterruptedException{

        if (!this.receiveDelayPool.isEmpty()) {
            Message msg = this.receiveDelayPool.poll();
            this.clock.update(msg.getTimestamp());
            return msg;
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
                this.clock.update(msg.getTimestamp());
                return msg;
            }
        }
    } 
    
    /* Move implementation to logger's driver
    public TimeStampedMessage receiveTimeStampedMessage() throws InterruptedException {
    	Message msg = this.controller.takeReceivedMessage();
    	TimeStampedMessage tsMsg = new TimeStampedMessage(msg.getTimestamp(), msg);
    	return tsMsg;
    }
    */
    

}
