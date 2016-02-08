package edu.cmu.ds.clock;

import java.io.Serializable;

public class LogicalTimestamp implements ITimestamp, Serializable {
    private int time;
    public LogicalTimestamp() {
        time = 0;
    }

    @Override
    public ITimestamp next() {
        this.time += 1;
        LogicalTimestamp tmp = new LogicalTimestamp();
        tmp.time = this.time;
        return tmp;
    }

    @Override
    public ITimestamp update(ITimestamp t) {
        time = Math.max(time, ((LogicalTimestamp)t).time);
        LogicalTimestamp tmp = new LogicalTimestamp();
        tmp.time = this.time;
        return tmp;
    }

    @Override
    public int compareTo(Object o) {
        if (time < ((LogicalTimestamp)o).time)
            return -1;
        if (time > ((LogicalTimestamp)o).time)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "[LogicalTimestamp: " + this.time + "]";
    }
    
    @Override
    public boolean equals(Object o) {
    	return this.compareTo((LogicalTimestamp)o) == 0;
    }
}
