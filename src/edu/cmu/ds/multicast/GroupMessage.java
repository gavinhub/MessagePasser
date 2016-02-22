package edu.cmu.ds.multicast;

import edu.cmu.ds.message.Message;

public class GroupMessage extends Message{
    protected String group;
    private String origin;

    public GroupMessage() {}

    public GroupMessage(String src, String group, String kind, String content) {
        super(src, null, kind, content);
        this.group = group;
        
        /* 
         * If it is the first time to send, set the origin the same as the src.
         */
        if (origin == null) {
        	origin = src;
        }
    }

    public String getGroup() {return group;}
    
    public String getOrigin() {return origin;}

    public void setGroup(String group) { this.group = group;}

    /**
     * Use timestamp plus the hash code of origin to build the hash code for this class.
     * @return int
     */
    public int hashCode() {
        return this.timestamp.getTime() + this.origin.hashCode();
    }

    /**
     * If both the timestamp and origin are equal, then two Groupmessage will be consider equal.
     * @return boolean
     */
    public boolean equals(Object o) {
    	GroupMessage target = (GroupMessage) o;
        if (this.getOrigin().equals(target.getOrigin()) && this.timestamp.getTime() == target.timestamp.getTime()) {
        	return true;
        }
        return false;
    }

    public GroupMessage copy() {
        GroupMessage gm = new GroupMessage();
        gm.sourceName = this.sourceName;
        gm.targetName = this.targetName;
        gm.content = this.content;
        gm.seqNum = this.seqNum;
        gm.timestamp = this.timestamp.copy();
        gm.group = this.group;
        gm.origin = this.origin;
        gm.kind = this.kind;
        return gm;
    }

    public String toString() {
        return this.content;
    }

}
