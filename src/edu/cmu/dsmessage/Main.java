package edu.cmu.dsmessage;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException, IOException{
        if (args.length < 2)
            throw new ParseException("Arguments error:" + args[0], 0);

        String configFile = args[0];
        String myName = args[1];

        MessagePasser passer = new MessagePasser(configFile, myName);
    }
}
















