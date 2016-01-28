package edu.cmu.dsmessage;

import java.io.*;
import java.text.ParseException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParseException, IOException{
        if (args.length < 2)
            throw new ParseException("Arguments error:" + args[0], 0);

        String configFile = args[0];
        String myName = args[1];

        MessagePasser passer = new MessagePasser(configFile, myName);

        /*
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
            Thread.sleep(500);
            System.out.print(">> ");
            String input = keyIn.readLine();
            if (input == null) {
                break;
            }
            if (input.trim().equals(""))
                continue;
            
            if (input.equals("receive")) {
            	Message msg = passer.receive();
            	System.out.println("[" + msg.getSourceName() + "] " + msg.getContent());
            } else {            
	            String name = input.split(" ")[0];
	            String content = input.split(" ", 1)[1]; // Update 01/28
	            Message msg = new Message(myName, name, content);
	            passer.send(msg);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
















