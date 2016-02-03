package edu.cmu.ds.clock;

public class ClockServiceFactory {

    public ClockService getClockService(String type) {
        switch (type) {
            case "LOGICAL":
                return new LogicalClock();
            case "VECTOR":
                return new VectorClock();
            default:
                return null;
        }
    }
}
