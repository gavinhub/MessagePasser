package edu.cmu.ds.message;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.clock.ClockServiceFactory;
import edu.cmu.ds.message.util.MLogger;

import java.io.*;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException, IOException{
        if (args.length < 2)
            throw new ParseException("Arguments error:" + args[0], 0);

        String configFile = args[0];
        String clockType = args[1];
        String myName = args[2];

        ClockServiceFactory factory = new ClockServiceFactory();
        ConfigParser config = new ConfigParser(configFile);

        ClockService clock = factory.getClockService(clockType, myName, config.getHostArray());
        MessagePasser passer = new MessagePasser(config, myName, clock);
        
        /* Auto receiving
         *
        Thread display = new Thread() {
            public void run() {
                while(true) {
                    try {
                    	Message msg = passer.receive();
                    	System.out.println("[" + msg.getSourceName() + "] " + msg.getContent());
                    	
                    } catch (InterruptedException e) {
                    	System.out.println("Interrupted");
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        display.start();
        */

        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        try {
            Thread.sleep(250);
            System.out.print("\033[92m>> ");
            String input = keyIn.readLine();
            
            
            if (input == null) {
                break;
            }
            if (input.trim().equals(""))
                continue;
            
            // ********* log test *********
            String[] part = input.split(" ");
            // If a user explicitly says "log", the message will be logged.
            if (part[0].equals("log")) {
            	String name = part[1];
            	String content = part[2];
            	Message logMsg = new Message(myName, "logger", content);
            	passer.send(logMsg);
            	Message msg = new Message(myName, name, content);
            	passer.send(msg);            	
            }
            // ********* End log test *********
            else if (input.equals("R")) {
            	Message msg = passer.receive();
                MLogger.info(msg.getSourceName() + " " + msg.getSequenceNumber(), msg.getContent() + " " + msg.timestampString());
            } else {            
	            String name = input.split(" ")[0];
	            String content = input.split(" ", 2)[1]; // Update 01/28
	            Message msg = new Message(myName, name, content);
	            passer.send(msg);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
















