package edu.cmu.ds.multicast;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.clock.ClockServiceFactory;
import edu.cmu.ds.clock.VectorClock;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Message;
import edu.cmu.ds.message.MessagePasser;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MulticastMessagePasser2 extends MessagePasser {

    private Map<String, Group> groups = new HashMap<>();
    private Deliverable mailBox;

    public MulticastMessagePasser2(ConfigParser parser, String myName, ClockService uniClock, Deliverable mail) throws ParseException, FileNotFoundException {
        super(parser, myName, uniClock);
        List<Group> groups = parser.getGroupList();
        ClockServiceFactory factory = new ClockServiceFactory();
        mailBox = mail;
        for (Group g : groups) {
            g.setClock((VectorClock) factory.getClockService("VECTOR", myName, parser.getHostArray()));
            this.groups.put(g.getName(), g);
        }
    }

    /*
    @Override
    public void send(Message msg) {
        GroupMessage gmsg = (GroupMessage) msg;
        String group = gmsg.group;
        // TODO: B-multicast
        
    }
    */
    
    
    /**
     * Implement multicast by calling send API for all members.
     * @param msg Message with only src, dest, content
     */
    public void multiSend(Message msg) {
    	GroupMessage gmsg = (GroupMessage) msg;
    	String dest = gmsg.getTargetName();
    	String content = gmsg.getContent();
    	String groupName = gmsg.getGroup();
    	Group group = groups.get(groupName);
    	List<String> members = group.getMembers();
    	
    	for (String i : members) {
    		Message specifiedMsg = new Message(i, dest, content);
    		send(specifiedMsg);
    	}
    }
    
    
    protected void listenOneMail() {
        // TODO: listening on receive. deliver to mailBox when possible
    	
    }
}
