package edu.cmu.dsmessage.except;

/**
 * Created by gavin on 1/19/16.
 */
public class StreamNotFoundException extends Exception{
    public StreamNotFoundException() {
        super();
    }

    public StreamNotFoundException(String s) {
        super(s);
    }
}
