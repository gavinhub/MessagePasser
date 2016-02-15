package edu.cmu.ds.multicast;

import edu.cmu.ds.message.Message;

public class GroupMessage extends Message {
    protected String group;

    public GroupMessage() {}

    public GroupMessage(String src, String group, String content) {
        super(src, null, content);
        this.group = group;
    }

    public String getGroup() {return group;}

    public void setGroup(String group) { this.group = group;}

    public int hashCode() {
        // TODO: same message will have same hashCode
        return 0;
    }

    public boolean equals(Object o) {
        // TODO: return true if the messages are same
        return true;
    }
}
