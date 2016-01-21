package edu.cmu.dsmessage;

import edu.cmu.dsmessage.except.StreamNotFoundException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Created by gavin on 1/17/16.
 */
class Controller {
    private Map<String, Host> hosts;
    private String myName;
    private BlockingQueue<Message> sendQueue; // Speaker will send out all messages in this queue
    private BlockingQueue<Message> receiveQueue; // store everything received.

    public Controller(ConfigParser parser, String myName) {
        sendQueue = new LinkedBlockingDeque<>();
        receiveQueue = new LinkedBlockingDeque<>();
        this.hosts = parser.getHosts();
        this.myName = myName;
    }

    /**
     * Append a message to the sending queue. Speaker will send all the messages in this queue
     * @param msg the message to be sent
     */
    public void appendSendingMessage(Message msg) {
        sendQueue.add(msg);
    }

    /**
     * Get the first message in the received messages queue. This method will block while the queue is empty until it's
     * not empty.
     * @return the first element in the queue.
     * @throws InterruptedException
     */
    public Message takeReceivedMessage() throws InterruptedException{
        return receiveQueue.take();
    }

    /**
     * Retrieve the first To-Be-Sent message from the queue. Will block until the queue is not empty.
     * Used by Speaker.
     * @return the first message in sending queue
     */
    protected Message retrieveSendingMsg() {
        while (true) {
            try {
                try {
                    return sendQueue.take();
                } catch (InterruptedException e) {
                    System.out.println("retrieve interrupted");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Add a message into the received-message queue.
     * ListenerSession will add all message it receives
     * @param msg message received.
     */
    protected void appendReceivedMessage(Message msg) {
        receiveQueue.add(msg);
    }

    public void addInputStream(String name, ObjectInputStream iStream) throws StreamNotFoundException {
        if (!this.hosts.containsKey(name))
            throw new StreamNotFoundException("Stream Not set");
        this.hosts.get(name).setInputStream(iStream);
    }

    public void addOutputStream(String name, ObjectOutputStream oStream) throws StreamNotFoundException {
        if (!this.hosts.containsKey(name))
            throw new StreamNotFoundException("Stream Not set");
        this.hosts.get(name).setOutputStream(oStream);
    }

    public ObjectInputStream getInputStream(String name) {
        Host host = this.hosts.get(name);
        if (host == null) return null;
        return host.getInputStream();
    }

    public ObjectOutputStream getOutputStream(String name) {
        Host host = this.hosts.get(name);
        if (host == null) return null;
        return host.getOutputStream();
    }

    /**
     * Find a host by name.
     * @param name the name of the host
     * @return null if doesn't exist.
     */
    public Host lookUpHost(String name) {
        if (hosts.containsKey(name))
            return this.hosts.get(name);
        return null;
    }

    public int getMyPortNumber() {
        return this.hosts.get(this.myName).getPortNumber();
    }

    public InetAddress getMyAddr() {
        return this.hosts.get(this.myName).getAddr();
    }

    public String getMyName() {
        return this.hosts.get(this.myName).getHostname();
    }

    /**
     * Print out a message. Just a test method.
     * @param msg the message to be handled.
     */
    public void handleMessage(Message msg) {
        System.out.println("[" + msg.getSourceName() + "] " + msg.getContent());
    }
}
