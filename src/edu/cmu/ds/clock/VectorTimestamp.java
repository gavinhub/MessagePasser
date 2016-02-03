package edu.cmu.ds.clock;

public class VectorTimestamp implements ITimestamp {
    private String mySelf;
    private int time;

    public VectorTimestamp(String mySelf) {
        this.mySelf = mySelf;
        this.time = 0;
    }

    @Override
    public ITimestamp next() {
        // TODO: implement next() method to obtain next Timestamp with the help of this.mySelf
        return null;
    }

    @Override
    public ITimestamp update(ITimestamp t) {
        // TODO: update timestamp
        return null;
    }

    @Override
    public int compareTo(Object o) {
        // TODO: compare timestamp with the help of this.mySelf
        return 0;
    }
}
