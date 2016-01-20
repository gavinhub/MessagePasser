package edu.cmu.dsmessage;

import java.io.*;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gavin on 1/17/16.
 */
class ConfigParser {
    private Host myself;
    private Map<String, Host> hosts;

    public ConfigParser(String configFile) throws ParseException {
        hosts = new HashMap<>();
        parse(configFile);
        System.out.println("Config Complete, " + hosts.size() + " hosts configured.");
    }

    protected void parse(String filename) throws ParseException {
        try {
            FileReader file = new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);
            String aLine;
            while ((aLine = reader.readLine()) != null) {
                String [] parts = aLine.split("[\\s:]+");
                if (parts.length != 3) {
                    throw new ParseException("Error Line " + parts.length + " " + aLine, 0);
                }
                Host host = new Host(InetAddress.getByName(parts[1]), parts[0], Integer.parseInt(parts[2]));
                hosts.put(parts[0], host);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParseException("IOException", 0);
        }

    }

    public Map<String, Host> getHosts() {
        return this.hosts;
    }
}
