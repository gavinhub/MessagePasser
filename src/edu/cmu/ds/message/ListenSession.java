package edu.cmu.ds.message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

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
