package edu.cmu.ds.mux;

/**
 * A Locker is a component used for resource requesting and releasing.
 */
public interface ILocker {
    boolean request() throws InterruptedException;
    boolean release();
    void info();
    public void handleReceivedMessage() throws InterruptedException;
}
