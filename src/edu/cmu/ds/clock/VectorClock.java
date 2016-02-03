package edu.cmu.ds.clock;

public class VectorClock extends ClockService {
    public VectorClock() {
        this.stamp = new VectorTimestamp(this.mySelf);
    }
}
