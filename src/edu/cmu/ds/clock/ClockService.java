package edu.cmu.ds.clock;

public abstract class ClockService {
    protected String mySelf;
    protected ITimestamp stamp;
    protected int host_num;

    public ITimestamp getCurrentTimestamp() {
        return this.stamp;
    }

    public ITimestamp next() {
        stamp = stamp.next();
        return stamp;
    }

    public ITimestamp update(ITimestamp comingTime) {
        stamp = stamp.update(comingTime);
        return stamp;
    }

    public String getMySelf() {
        return mySelf;
    }
}
