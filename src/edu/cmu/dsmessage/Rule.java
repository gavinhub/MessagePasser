package edu.cmu.dsmessage;


public class Rule {
    public static enum Action {
        DROP, DROPAFTER, DELAY, NOTSET;
    }

    // no need for setter and getter
    public Action action;
    public String src;
    public String dest;
    public int seqNum;

    public Rule(Action act) {
        action = act;
        src = null;
        dest = null;
        seqNum = -1;
    }

    public boolean match(Message msg) {
        if (src != null && !msg.getSourceName().equals(src)) return false;
        if (dest != null && !msg.getTargetName().equals(dest)) return false;
        if (action == Action.DROPAFTER && msg.getSequenceNumber() <= seqNum) return false;

        return true;
    }
}
