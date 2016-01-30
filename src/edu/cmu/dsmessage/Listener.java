package edu.cmu.dsmessage;

import edu.cmu.dsmessage.except.StreamNotFoundException;
import edu.cmu.dsmessage.util.Logger;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Listener implements Runnable{

    private ServerSocket server;
    private Controller ctrl;

    public Listener(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.ctrl.getMyPortNumber());
            Logger.log("LISTEN", "Listening on port: " + this.ctrl.getMyPortNumber());
            while (true) {
                Socket coming = this.server.accept();

                // response
                Message response = new Message(this.ctrl.getMyName(), "unknown", "Connect");
                ObjectOutputStream oStream = new ObjectOutputStream(coming.getOutputStream());
                oStream.writeObject(response);
                oStream.flush();
                Logger.customOput("\033[36m[INFO] Connection Request Accepted\n\033[92m>> ");
                ObjectInputStream iStream = new ObjectInputStream(coming.getInputStream());
                Message firstMessage = (Message) iStream.readObject();
                this.ctrl.addOutputStream(firstMessage.getSourceName(), oStream, firstMessage.getSourceName());
                this.ctrl.appendReceivedMessage(firstMessage);
                this.ctrl.addInputStream(firstMessage.getSourceName(), iStream, firstMessage.getSourceName());

                Thread talk = new Thread(new ListenSession(this.ctrl, firstMessage.getSourceName(), iStream));
                talk.start();
            }
        } catch (IOException | ClassNotFoundException | StreamNotFoundException e) {
            e.printStackTrace();
        }
    }
}
