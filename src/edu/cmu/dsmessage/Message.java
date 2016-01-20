package edu.cmu.dsmessage;

import java.io.Serializable;

/**
 * Created by gavin on 1/19/16.
 */
public class Message implements Serializable{
    private String sourceName, targetName;
    private String content;

    public Message() {}
    public Message(String src, String target, String content) {
        this.sourceName = src;
        this.targetName = target;
        this.content = content;
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
}
