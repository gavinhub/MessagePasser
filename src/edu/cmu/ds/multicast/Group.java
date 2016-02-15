package edu.cmu.ds.multicast;

import edu.cmu.ds.clock.VectorClock;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private List<String> members;
    private VectorClock clock;

    public Group() {}

    public Group(String groupName) {
        this.groupName = groupName;
        this.members = new ArrayList<>();
    }

    public void setClock(VectorClock vc) {
        clock = vc;
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
}
