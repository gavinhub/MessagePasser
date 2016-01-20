package edu.cmu.dsmessage;


import edu.cmu.dsmessage.except.StreamNotFoundException;

import java.io.*;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print(">>");
                String input = keyIn.readLine();
                if (input == null) {
                    break;
                }
                String name = input.split(" ")[0];
                ObjectOutputStream oStream = this.ctrl.getOutputStream(name);
                if (oStream == null) {
                    Host targetHost = this.ctrl.lookUpHost(name);
                    if (targetHost == null) throw new StreamNotFoundException("No such host");
                    Socket socket = new Socket(targetHost.getAddr(), targetHost.getPortNumber());
                    ObjectInputStream iStream = new ObjectInputStream(socket.getInputStream());
                    Message response = (Message)iStream.readObject();
                    oStream = new ObjectOutputStream(socket.getOutputStream());

                    this.ctrl.handleMessage(response);
                    this.ctrl.addOutputStream(name, oStream);
                    this.ctrl.addInputStream(name, iStream);
                    Thread session = new Thread(new ListenSession(this.ctrl, iStream));
                    session.start();
                }
                Message msg = new Message(this.ctrl.getMyName(), name, input);
                oStream.writeObject(msg);
            } catch (IOException | StreamNotFoundException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
