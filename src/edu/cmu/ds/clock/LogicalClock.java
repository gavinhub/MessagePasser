package edu.cmu.ds.clock;

public class LogicalClock extends ClockService {
    public LogicalClock() {
        this.stamp = new LogicalTimestamp();
    }

    @Override
    public void increase(String name) {
    }
}
