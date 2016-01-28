package edu.cmu.dsmessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
	protected Map<String, Integer> getReceiveDropAfterRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("receiveRules");
		Map<String, Integer> receiveRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("dropAfter")) {
				String src = (String)map.get("src");
				Integer seqNum = (Integer)map.get("seqNum");
				if (!receiveRules.containsKey(src)) {
					receiveRules.put(src, seqNum);
				} else {
					System.out.println("Error : You cannot define two sequence number");
				}
			} 			
		}
		return receiveRules;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,Set<String>> getSendDelayRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("sendRules");
		Map<String, Set<String>> sendRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("delay")) {
				if (map.containsKey("src") && map.get("src") != null &&
					map.containsKey("dest") && map.get("dest") != null) {
					String src = (String)map.get("src");
					String dest = (String)map.get("dest");
					if (sendRules.containsKey(dest)) {
						sendRules.get(dest).add(src);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(src);
						sendRules.put(dest, set);
					}
				} else if (map.containsKey("dest") && map.get("dest") != null) {
					String dest = (String)map.get("dest");
					sendRules.put(dest, null);
				}
				
			} 			
		}		
		return sendRules;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Set<String>> getReceiveDelayRules(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
				.load(new FileInputStream(new File(file)));
		
		ArrayList<Map<String, Object>> list =  values.get("receiveRules");
		Map<String, Set<String>> receiveRules = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String action = (String)map.get("action");
			if (action.equals("delay")) {
				if (map.containsKey("src") && map.get("src") != null &&
						map.containsKey("dest") && map.get("dest") != null) {
						String src = (String)map.get("src");
						String dest = (String)map.get("dest");
						if (receiveRules.containsKey(src)) {
							receiveRules.get(src).add(dest);
						} else {
							Set<String> set = new HashSet<String>();
							set.add(dest);
							receiveRules.put(src, set);
						}
					} else if (map.containsKey("src") && map.get("src") != null) {
						String src = (String)map.get("src");
						receiveRules.put(src, null);
					}
			} 			
		}		
		return receiveRules;
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		YamlReader reader = new YamlReader();
		System.out.println("ss");
		
		Map<String, Set<String>> receive = reader.getReceiveDelayRules("configuration.yaml");
		Iterator it = receive.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			String src = (String) entry.getKey();
			Set<String> set = (Set) entry.getValue();
			System.out.println("src = " + src + "\t set = " + set);
		}
		
		System.out.println("---------");
		Map<String, Set<String>> send = reader.getSendDelayRules("configuration.yaml");
		Iterator iter = send.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Entry) iter.next();
			String dest = (String) entry.getKey();
			Set<String> set = (Set) entry.getValue();
			System.out.println("dest = " + dest + "\t set = " + set);
		}
		
	}
	
}
