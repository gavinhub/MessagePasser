package edu.cmu.dsmessage;

import edu.cmu.dsmessage.except.StreamNotFoundException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;


/**
 * Created by gavin on 1/17/16.
 */
class Controller {
    private Map<String, Socket> connections;
    private Map<String, Host> hosts;
    private String myName;

    public Controller(ConfigParser parser, String myName) {
        this.hosts = parser.getHosts();
        this.myName = myName;
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

    public void handleMessage(Message msg) {
        System.out.println("[" + msg.getSourceName() + "] " + msg.getContent());
    }
}
