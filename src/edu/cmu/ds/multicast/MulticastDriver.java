package edu.cmu.ds.multicast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.clock.ClockServiceFactory;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Message;
import edu.cmu.ds.message.MessagePasser;
import edu.cmu.ds.message.util.MLogger;

public class MulticastDriver {
	
	public static void main(String[] args) throws ParseException, FileNotFoundException {
		if (args.length < 2)
            throw new ParseException("Arguments error:" + args[0], 0);

        String configFile = args[0];
        String clockType = args[1];
        String myName = args[2];

        ClockServiceFactory factory = new ClockServiceFactory();
        ConfigParser config = new ConfigParser(configFile);

        ClockService clock = factory.getClockService(clockType, myName, config.getHostArray());
        MulticastMessagePasser passer = new MulticastMessagePasser(config, myName, clock);
        
        /* 
         * Get all the groups and the groups that this user belongs to.
         */        
        List<Group> groups = config.getGroupList();
        List<Group> myGroups = config.getMyGroups(myName);
        
        ///////// Test /////////
        System.out.print("Groups all over the network: ");
        for (Group g : groups) {
        	System.out.print(g.getName() + "\t");
        }
        System.out.println();
        
        System.out.print("My groups: ");
        for (Group g : myGroups) {
        	System.out.println(g.getName() + "\t");
        }
        ///////// End Test /////////
        
        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        try {
            Thread.sleep(250);
            System.out.print("\033[92m>> ");
            String input = keyIn.readLine();
            
            
            if (input == null) {
                break;
            }
            if (input.trim().equals("")) {
                continue;
            } else if (input.equals("D")) {
            	Message msg = passer.deliver();
             	MLogger.info(msg.getSourceName() + " " + msg.getSequenceNumber(), msg.getContent() + " " + msg.timestampString());
            } else {
	            String groupName = input.split(" ")[0];
	            String content = input.split(" ", 2)[1]; // Update 01/28
	            GroupMessage msg = new GroupMessage(myName, groupName, content);
	            passer.multiCast(msg);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}
}
