package edu.cmu.ds.clock;

public class VectorClock extends ClockService {
    public VectorClock(String mySelf, String [] hosts) {
        this.mySelf = mySelf;
        this.stamp = new VectorTimestamp(mySelf, hosts);
    }
}
