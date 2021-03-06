package edu.cmu.ds.mux;


import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.clock.ClockServiceFactory;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.util.MLogger;
import edu.cmu.ds.multicast.MulticastMessagePasser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

public class MuxDriver {

    /**
     * User Interface
     * arguments: configFile localName
     * Usage:
     *      REQ - request for critical section
     *              program will notify user when success or failed.
     *      RLS - release the critical section
     *              program will notify user when success or failed.
     * Note:
     *      Information
     * @param args
     */
    public static void main(String [] args) throws FileNotFoundException, ParseException {


        String configFile = args[0];
        String myName = args[1];

        ClockServiceFactory factory = new ClockServiceFactory();
        ConfigParser config;
        ILocker locker;
        CriticalSection cs = new CriticalSection();
        try {
            config = new ConfigParser(configFile);
            ClockService clock = factory.getClockService("VECTOR", myName, config.getHostArray());

            locker =  new ResLocker(config, myName, clock, cs);
        } catch (ParseException | FileNotFoundException e) {
            MLogger.error("Parsing", "File error");
            e.printStackTrace();
            return;
        }
        
        
        Thread receiver = new Thread() {
            public void run() {
                while(true) {
                    try {
                    	locker.handleReceivedMessage();
                    } catch (InterruptedException e) {
                    	System.out.println("Interrupted");
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        receiver.start();
        
        
        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
        while (true)
            try {
                Thread.sleep(100);
                System.out.print("\033[92m>> ");
                String input = keyIn.readLine();

                if (input == null) {
                    break;
                }
                if (input.trim().equals("")) {
                    continue;
                } else if (input.equals("REQ")) {
                    if (locker.request()) {
                        MLogger.message("Request", "Request sent");
                    } else {
                        MLogger.message("Request", "Requesting, Please wait.");
                    }

                } else if (input.equals("RLS")) {
                    if (locker.release()) {
                        MLogger.message("Release", "Release sent");
                    } else {
                        MLogger.message("Release", "Waiting.");
                    }
                } else if (input.equals("INFO")) {
                    locker.info();
                }
            } catch (InterruptedException | IOException e1) {
                e1.printStackTrace();
            }
    }
}
