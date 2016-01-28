package edu.cmu.dsmessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

/**
 * Created by gavin on 1/17/16.
 */
public class Host {
    private InetAddress addr;
    private int port;
    private String hostname;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private Integer seqNum;

    public Host(InetAddress addr, String name, int port) {
        this.addr = addr;
        this.hostname = name;
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

    public void setInputStream(ObjectInputStream iStream) {
        this.iStream = iStream;
    }

    public void setOutputStream(ObjectOutputStream oStream) {
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
}
