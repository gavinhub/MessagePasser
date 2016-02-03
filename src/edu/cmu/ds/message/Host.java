package edu.cmu.ds.message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class Host {
    private InetAddress addr;
    private int port;
    private String hostname;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private Integer seqNum;
    private String connector;

    public Host(InetAddress addr, String name, int port) {
        this.addr = addr;
        this.hostname = name;
        this.connector = null;
        this.port = port;
        this.oStream = null;
        this.iStream = null;
        this.seqNum = 0;
    }

    public InetAddress getAddr() {
        return this.addr;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPortNumber() {
        return this.port;
    }
    
    public int getSeqNum() {
    	return this.seqNum;
    }

    public synchronized void setInputStream(ObjectInputStream iStream, String connector) {
        if (this.connector == null || this.connector.compareTo(connector) < 0) {
            this.connector = connector;
            this.iStream = iStream;
        }
    }

    public synchronized void setOutputStream(ObjectOutputStream oStream, String connector) {
        if (this.connector == null || this.connector.compareTo(connector) < 0)
        this.oStream = oStream;
    }

    public ObjectInputStream getInputStream() {
        return this.iStream;
    }

    public ObjectOutputStream getOutputStream() {
        return this.oStream;
    }
    
    public void increaseSeqNum() {
    	this.seqNum++;
    }

    public void detach() {
        this.iStream = null;
        this.oStream = null;
        this.connector = null;
    }
}
