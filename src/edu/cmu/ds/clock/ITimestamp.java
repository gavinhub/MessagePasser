package edu.cmu.ds.clock;

public interface ITimestamp extends Comparable{
    ITimestamp next();
    ITimestamp update(ITimestamp t);
}
