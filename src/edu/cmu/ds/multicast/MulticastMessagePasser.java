package edu.cmu.ds.multicast;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.clock.ClockServiceFactory;
import edu.cmu.ds.clock.VectorClock;
import edu.cmu.ds.clock.VectorTimestamp;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Message;
import edu.cmu.ds.message.MessagePasser;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MulticastMessagePasser extends MessagePasser {

    private Map<String, Group> groups = new HashMap<>();

    public MulticastMessagePasser(ConfigParser parser, String myName, ClockService uniClock) throws ParseException, FileNotFoundException {
        super(parser, myName, uniClock);
        List<Group> groups = parser.getGroupList(myName);
        ClockServiceFactory factory = new ClockServiceFactory();
        for (Group g : groups) {
            g.setClock((VectorClock) factory.getClockService("VECTOR", myName, parser.getHostArray()));
            this.groups.put(g.getName(), g);
        }
    }

    /**
     * The message should have `group`; Doesn't care about timestamp
     * @param gmsg msg with group
     */
    protected void B_multicast(GroupMessage gmsg) {
        assert gmsg.group != null;
        Group group = groups.get(gmsg.getGroup());
        for (String target: group.getMembers()) {
            gmsg.setTargetName(target);
            this.sendTimedMessage(gmsg);
        }
    }

    /**
     * Reliable multicast. Simply call B_multicast; The deliver process is different
     * @param gmsg msg with group
     */
    protected void R_multicast(GroupMessage gmsg) {

    }

    /**
     * Increase group timestamp before sending.
     * @param gmsg
     */
    protected void CO_multicast(GroupMessage gmsg) {

    }

    /**
     * Deliver whenever message received.
     * @return blocked if no message delivered
     */
    protected GroupMessage B_deliver() throws InterruptedException {
        return (GroupMessage) this.receive();
    }

    /**
     * Reliable delivery. Make sure to eliminate duplications and deliver after multicast it.
     * @return block if no message to deliver
     */
    protected GroupMessage R_deliver() {
        return null;
    }

    /**
     * Using a hold back queue to maintain casual ordering
     * @return block if no message to deliver
     */
    protected GroupMessage CO_deliver() {
        return null;
    }

    /**
     * This GroupMessage is already assigned with an group
     * @param msg msg.group != null
     */
    public void multiCast(GroupMessage msg) {
        assert msg.group != null;
        Group group = groups.get(msg.group);
        VectorTimestamp time = group.getNextTimestamp();
        msg.setTimestamp(time);
        for (String target: group.getMembers()) {
            msg.setTargetName(target);
            this.sendTimedMessage(msg);
        }
    }

}
