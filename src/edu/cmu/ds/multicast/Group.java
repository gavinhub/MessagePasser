package edu.cmu.ds.multicast;

import edu.cmu.ds.clock.VectorClock;
import edu.cmu.ds.clock.VectorTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Group {
    private String groupName;
    private List<String> members;
    private VectorClock clock;
    public Queue<GroupMessage> holdback = new LinkedBlockingQueue<>();

    public Group() {}

    public Group(String groupName) {
        this.groupName = groupName;
        this.members = new ArrayList<>();
    }

    public void setClock(VectorClock vc) {
        clock = vc;
    }

    public VectorTimestamp getNextTimestamp() {
        return (VectorTimestamp) clock.next();
    }

    public void add(String member) {
        members.add(member);
    }

    public int size() {
        return members.size();
    }

    public List<String> getMembers() {
        return members;
    }

    public String getName() {
        return groupName;
    }

    public int getTimeStamp(String name) {
        return ((VectorTimestamp)clock.getCurrentTimestamp()).getTimeStamp(name);
    }

    public void increaseTime(String name){
        this.clock.increase(name);
    }

    public void addMessage(GroupMessage gm) {
        holdback.add(gm);
    }

    public GroupMessage pollMessage() {
        return holdback.poll();
    }
}
