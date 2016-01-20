package edu.cmu.dsmessage;

import edu.cmu.dsmessage.except.StreamNotFoundException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by gavin on 1/19/16.
 */
class ListenSession implements Runnable {
    private Controller ctrl;
    private ObjectInputStream iStream;

    public ListenSession(Controller ctrl, ObjectInputStream iStream) {
        this.ctrl = ctrl;
        this.iStream = iStream;
    }

    public void run() {
        try {
            while (true) {
                Message msg = (Message)this.iStream.readObject();
                this.ctrl.appendReceivedMessage(msg);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
