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
    
    /**
     * Return an int as part of the hashcode for GroupMessage.
     * @return int
     */
    public int getTime() {
    	return time;
    }

    @Override
    public ITimestamp copy() {
        LogicalTimestamp a = new LogicalTimestamp();
        a.time = this.time;
        return a;
    }

    @Override
    public void increase(String name) {
    }
}
