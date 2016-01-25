package edu.cmu.dsmessage;

import edu.cmu.dsmessage.except.StreamNotFoundException;

import java.io.EOFException;
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
    private String myName;

    public ListenSession(Controller ctrl, String myName, ObjectInputStream iStream) {
        this.ctrl = ctrl;
        this.iStream = iStream;
        this.myName = myName;
    }

    public void run() {
        try {
            while (true) {
                Message msg = (Message)this.iStream.readObject();
                this.ctrl.appendReceivedMessage(msg);
            }
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof EOFException) {
                this.ctrl.removeStreams(this.myName);
                return;
            }
            e.printStackTrace();
        }
    }

}
