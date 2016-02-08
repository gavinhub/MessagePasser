package edu.cmu.ds.application;

import edu.cmu.ds.message.Message;

public interface DSApplication {
    public void onMessage(Message msg);
}
