package edu.cmu.ds.multicast;

import edu.cmu.ds.message.Message;

public interface Deliverable {
    void deliver(Message msg);
}
