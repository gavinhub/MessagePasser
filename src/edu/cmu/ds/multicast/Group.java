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
    private String myName;
    private List<String> members;
    private VectorClock clock;
    public Queue<GroupMessage> holdback = new LinkedBlockingQueue<>();
    public int selfReceive = 0;

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

    public GroupMessage fetchQueuedMessage() {
        int qSize = holdback.size();
        for (int i = 0; i < qSize; i ++) {
            GroupMessage top = holdback.poll();
            VectorTimestamp ctime = (VectorTimestamp) top.getTimestamp();

            if (top.getOrigin().equals(myName) && selfReceive + 1 == ctime.getTimeStamp(myName)) {
                selfReceive += 1;
                return top;
            }

            else if (getTimeStamp(top.getOrigin()) + 1 == ctime.getTimeStamp(top.getOrigin())) {
                boolean valid = true;
                for (String member: getMembers()) {
                    if (!member.equals(top.getOrigin()) && getTimeStamp(member) < ctime.getTimeStamp(member)) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    increaseTime(top.getOrigin());
                    return top;
                }
            }

            else {
                addMessage(top);
            }
        }

        return null;
    }

    public void setMySelf(String mySelf) {
        this.myName = mySelf;
    }
}
