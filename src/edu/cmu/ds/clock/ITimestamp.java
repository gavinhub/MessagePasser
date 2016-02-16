package edu.cmu.ds.clock;

public interface ITimestamp extends Comparable{
    ITimestamp next();
    ITimestamp update(ITimestamp t);
    int getTime();
    ITimestamp copy();
    void increase(String name);
}
