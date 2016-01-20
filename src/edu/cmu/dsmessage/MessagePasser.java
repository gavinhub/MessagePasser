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

    public void send(Message msg) {
        // TODO: send messages according to the information stored in the Message object
        // TODO: remember to check the rules before sending.
    }

    public Message receive() {
        // TODO: poll messages from the queue.
        return null;
    }

}
