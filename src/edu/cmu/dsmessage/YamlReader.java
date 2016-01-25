package edu.cmu.dsmessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

class YamlReader {
	
	@SuppressWarnings("unchecked")
	protected ArrayList<Map<String, Object>> getConfig(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		return values.get("configuration");
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Set<String>> getSendDropRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("sendRules");
		Map<String, Set<String>> sendRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("drop")) {
				String src = (String)map.get("src");
				String dest = (String)map.get("dest");
				if (!sendRules.containsKey(src)) {
					Set<String> set = new HashSet<>();
					set.add(dest);
					sendRules.put(src, set);
				} else {
					sendRules.get(src).add(dest);
				}
			} 			
		}
		
		return sendRules;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Set<String>> getReceiveDropRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("receiveRules");
		Map<String, Set<String>> receiveRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("drop")) {
				String src = (String)map.get("src");
				String dest = (String)map.get("dest");
				if (!receiveRules.containsKey(src)) {
					Set<String> set = new HashSet<>();
					set.add(dest);
					receiveRules.put(src, set);
				} else {
					receiveRules.get(src).add(dest);
				}
			} 			
		}
				
		return receiveRules;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> getSendDropAfterRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("sendRules");
		Map<String, Integer> sendRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("dropAfter")) {
				String src = (String)map.get("src");
				Integer seqNum = (Integer)map.get("seqNum");
				if (!sendRules.containsKey(src)) {
					sendRules.put(src, seqNum);
				} else {
					System.out.println("Error : You cannot define two sequence number");
				}
			} 			
		}
		
		return sendRules;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> getSendDelayRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("sendRules");
		Map<String, Integer> sendRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("dropAfter")) {
				String src = (String)map.get("src");
				Integer seqNum = (Integer)map.get("seqNum");
				if (!sendRules.containsKey(src)) {
					sendRules.put(src, seqNum);
				} else {
					System.out.println("Error : You cannot define two sequence number");
				}
			} 			
		}
		
		return sendRules;
	}
	
}
