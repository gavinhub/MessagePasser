package edu.cmu.ds.clock;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class VectorTimestamp implements ITimestamp, Serializable {
    private String mySelf;
    private LinkedHashMap<String, Integer> time;

    public VectorTimestamp(String mySelf) {
        this.mySelf = mySelf;
        this.time = new LinkedHashMap<>();
    }

    public VectorTimestamp(String mySelf, LinkedHashMap<String, Integer> oldTime) {
        this.mySelf = mySelf;
        this.time = new LinkedHashMap<>(oldTime);
    }

    public VectorTimestamp(String mySelf, String [] hosts) {
        this.mySelf = mySelf;
        this.time = new LinkedHashMap<>();
        for (String host: hosts) {
            this.time.put(host, 0);
        }
    }

    protected void setTimeStamp(String name, int number) {
        this.time.put(name, number);
    }

    public int getTimeStamp(String name) {
        return this.time.get(name);
    }
    
    /**
     * Return an int consisting of all the vector timestamps as part of the hashcode for GroupMessage.
     * @return int
     */
    public int getTime() {
    	int timeCode = 0;
    	Iterator iter = time.entrySet().iterator();
    	while(iter.hasNext()) {
    		Map.Entry entry = (Map.Entry) iter.next();
    		timeCode = timeCode * 10 + (int) entry.getValue();
    	}
    	return timeCode;
    }

    @Override
    public ITimestamp copy() {
        return new VectorTimestamp(this.mySelf, this.time);
    }

    @Override
    public void increase(String name) {
        time.put(name, time.get(name) + 1);
    }

    @Override
    public ITimestamp next() {
        // Obtain next Timestamp with the help of this.mySelf
        VectorTimestamp vector = new VectorTimestamp(this.mySelf, this.time);
        vector.time.put(this.mySelf, this.time.get(this.mySelf) + 1);
        return vector;
    }

    @Override
    public ITimestamp update(ITimestamp t) {
        VectorTimestamp target = (VectorTimestamp)t;
        for (Map.Entry<String, Integer> entry: target.time.entrySet()) {
            String name = entry.getKey();
            this.time.put(name, Math.max(entry.getValue(), this.time.get(name)));
            if (name.equals(this.mySelf))
                this.time.put(name, this.time.get(name) + 1);
        }
        return this;
    }

    @Override
    public int compareTo(Object o) {
        VectorTimestamp target = (VectorTimestamp)o;
        boolean before = false;
        boolean after = false;
        for (Map.Entry<String, Integer> entry: target.time.entrySet()) {
            String name = entry.getKey();
            if (this.time.get(name) < entry.getValue())
                before = true;
            if (this.time.get(name) > entry.getValue())
                after = true;
        }

        if (!before && !after) {
            return 0;
        } else if (before && after) {
            return this.mySelf.compareTo(target.mySelf);
        } else if (before) {
            return -1;
        } else {
            return 1;
        }
    }

    public int compare(String name, VectorTimestamp vt) {
        return this.getTimeStamp(name) - vt.getTimeStamp(name);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[VectorTimestamp: ");
        for (Map.Entry<String, Integer> entry: this.time.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }

}
