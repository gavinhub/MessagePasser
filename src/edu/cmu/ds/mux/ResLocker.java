package edu.cmu.ds.mux;

import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.multicast.MulticastMessagePasser;

/**
 * ResLocker - implemented with Maekawa's Algorithm
 *
 */
public class ResLocker implements ILocker {

    private MulticastMessagePasser passer;
    private String dedicatedGroup;

    public ResLocker(ConfigParser config, MulticastMessagePasser passer) {
        this.passer = passer;
        this.dedicatedGroup = config.getMuxGroup(passer.getMyName());
    }

    /**
     * request - request the resource. Block until the process get the resource
     * @return true when request success. false if the process is already in the Critical Section.
     */
    @Override
    public boolean request() {
        return false;
    }

    /**
     * release - release the resource.
     * @return false when not in CS; true when release successfully.
     */
    @Override
    public boolean release() {
        return false;
    }

    /**
     * Info - show information
     *  1. how many messages have been sent or received
     *  2. whether in CS
     */
    @Override
    public void info() {

    }
}
