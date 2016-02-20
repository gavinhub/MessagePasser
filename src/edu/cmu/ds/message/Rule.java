package edu.cmu.ds.message;


public class Rule {
    public static enum Action {
        DROP, DROPAFTER, DELAY, NOTSET;
    }

    // no need for setter and getter
    public Action action;
    public String src;
    public String dest;
    public int seqNum;
    public String kind;

    public Rule(Action act) {
        action = act;
        src = null;
        dest = null;
        kind = null;
        seqNum = -1;
    }

    public boolean match(Message msg) {
        if (src != null && !msg.getSourceName().equals(src)) return false;
        if (dest != null && !msg.getTargetName().equals(dest)) return false;
        if (kind != null && !msg.getKind().equals(kind)) return false;
        if (action == Action.DROPAFTER && msg.getSequenceNumber() <= seqNum) return false;
        return true;
    }
}
