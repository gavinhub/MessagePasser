package edu.cmu.ds.message;

import edu.cmu.ds.message.util.MLogger;
import edu.cmu.ds.multicast.Group;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigParser {
    private Map<String, Host> hosts;
	private String filename;
    private ArrayList<Group> groups;
	private Map<String, String> mapping;

    public ConfigParser(String configFile) throws ParseException, FileNotFoundException {
		this.filename = configFile;
        hosts = new HashMap<>();
        mapping = new HashMap<>();
        parse(configFile);
        MLogger.log("CONFIG", "Config Complete, " + hosts.size() + " hosts configured.");
    }

    protected void parse(String filename) throws ParseException, FileNotFoundException {
    	YamlReader reader = new YamlReader();
    	ArrayList<Map<String, Object>> config = reader.getConfig(filename);
    	
    	for (Map<String, Object> person : config) {
    		String name = (String)person.get("name");
    		String ip = (String)person.get("ip");
			String memberOf = (String)person.get("memberOf");
			if (memberOf != null) {
				mapping.put(name, memberOf);
			} else {
				throw new ParseException("no memberOf", 0);
			}
    		Host host = null;
			try {
				host = new Host(InetAddress.getByName(ip), name, (int)person.get("port"));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
    		hosts.put(name, host);
    	}
    	
        groups = reader.getGroups(filename);

		/* member of */

    }


    public Map<String, Host> getHosts() {
        return this.hosts;
    }

	public String [] getHostArray() {
		String [] hostList = new String[hosts.size()];
		return hosts.keySet().toArray(hostList);
	}

	public List<Group> getGroupList() {
        return this.groups;
    }
	
	/**
	 * return groups that contains this member
     * Note that a user may belong to several groups.     
	 * @param myName the member name
	 * @return groups containing this member
     */
	public List<Group> getMyGroups(String myName) {
		List<Group> myGroup = new ArrayList<>();
        for (Group g : groups) {
        	if (g.getMembers().contains(myName)) {
				g.setMySelf(myName);
        		myGroup.add(g);
        	}
        }
		return myGroup;
	}

	public String getFileName() {
		return this.filename;
	}

	public String getMuxGroup(String myName) {
		return mapping.get(myName);
	}
	
	public List<String> getMuxGroupMembers(String myName) {
		String groupName = getMuxGroup(myName);
		
		List<String> members = null;
		for (Group g : groups) {
			if (groupName.equals(g.getName())) {
				members = g.getMembers();
			} 
		}
		return members;
	}
}
