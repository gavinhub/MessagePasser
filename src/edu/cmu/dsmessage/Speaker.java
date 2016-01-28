package edu.cmu.dsmessage;


import edu.cmu.dsmessage.except.StreamNotFoundException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by gavin on 1/17/16.
 */
class Speaker implements Runnable {
    private Controller ctrl;

    public Speaker(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = this.ctrl.retrieveSendingMsg();
                String name = msg.getTargetName();
                ObjectOutputStream oStream = this.ctrl.getOutputStream(name);
                if (oStream == null) {
                    Host targetHost = this.ctrl.lookUpHost(name);
                    if (targetHost == null) throw new StreamNotFoundException("No such host");
                    Socket socket = new Socket(targetHost.getAddr(), targetHost.getPortNumber());
                    ObjectInputStream iStream = new ObjectInputStream(socket.getInputStream());
                    Message response = (Message)iStream.readObject();
                    oStream = new ObjectOutputStream(socket.getOutputStream());

                    this.ctrl.handleMessage(response);
                    this.ctrl.addOutputStream(name, oStream, this.ctrl.getMyName());
                    this.ctrl.addInputStream(name, iStream, this.ctrl.getMyName());
                    Thread session = new Thread(new ListenSession(this.ctrl, name, iStream));
                    session.start();
                }
                oStream.writeObject(msg);
            } catch (IOException | ClassNotFoundException e) {
                if (e instanceof ConnectException) {
                    System.out.println("[Exception] The other one is not ready.");
                } else {
                    e.printStackTrace();
                }
            } catch (StreamNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
