package edu.cmu.dsmessage;

import java.io.Serializable;

public class Message implements Serializable{
    private String sourceName, targetName;
    private String content;
    private int seqNum;

    public Message() {}
    public Message(String src, String target,String content) {
        this.sourceName = src;
        this.targetName = target;
        this.content = content;
        this.seqNum = -1;
    }

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
}
