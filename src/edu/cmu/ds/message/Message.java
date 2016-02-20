package edu.cmu.ds.message;

import edu.cmu.ds.clock.ITimestamp;

import java.io.Serializable;

public class Message implements Serializable{
    protected String sourceName, targetName;
    protected String content;
    protected int seqNum;
    protected ITimestamp timestamp;
    protected String kind;

    public Message() {}
    public Message(String src, String target, String kind, String content) {
        this.sourceName = src;
        this.targetName = target;
        this.content = content;
        this.seqNum = -1;
        this.timestamp = null;
        this.kind = kind;
    }
    public Message(Message a) {
        sourceName = a.sourceName;
        targetName = a.targetName;
        content = a.content;
        seqNum = a.seqNum;
        kind = a.kind;
        timestamp = a.timestamp.copy();
    }

    public String getKind() { return kind;}

    public void setKind(String kind) {this.kind = kind;}

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSequenceNumber() {
        return seqNum;
    }

    public void setSequenceNumber(int seqNum) {
        this.seqNum = seqNum;
    }

    public ITimestamp getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(ITimestamp time) {
        this.timestamp = time;
    }

    public String timestampString() {
        return timestamp.toString();
    }

    public Message copy() {
        return new Message(this);
    }
}
