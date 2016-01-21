package edu.cmu.dsmessage;

import java.text.ParseException;

/**
 * Created by gavin on 1/19/16.
 */
public class MessagePasser {
    private Controller controller;

    public MessagePasser(String configFile, String myName) throws ParseException{
        ConfigParser parser = new ConfigParser(configFile);

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
        this.controller.appendSendingMessage(msg);
    }

    /**
     * poll out an element from the top of the queue.
     * @return Message
     */
    public Message receive() throws InterruptedException{
        // TODO: poll messages from the queue. You should check against the rolls. If dropped, poll next one.
        return controller.takeReceivedMessage();
    }

}
