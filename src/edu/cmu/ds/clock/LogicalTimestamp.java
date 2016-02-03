package edu.cmu.ds.clock;

public class LogicalTimestamp implements ITimestamp {
    private int time;
    public LogicalTimestamp() {
        time = 0;
    }

    @Override
    public ITimestamp next() {
        // TODO: implement next() method to obtain next Timestamp: add 1
        return null;
    }

    @Override
    public ITimestamp update(ITimestamp t) {
        // TODO: implement update() to update current timestamp according to coming timestamp
        return null;
    }

    @Override
    public int compareTo(Object o) {
        // TODO: compare timestamp
        return 0;
    }
}
