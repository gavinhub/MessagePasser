package edu.cmu.ds.clock;

public class ClockServiceFactory {

    public ClockService getClockService(String type, String mySelf, String [] hosts) {
        switch (type) {
            case "LOGICAL":
                return new LogicalClock();
            case "VECTOR":
                return new VectorClock(mySelf, hosts);
            default:
                return null;
        }
    }
}
