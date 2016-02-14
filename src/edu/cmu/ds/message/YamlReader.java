package edu.cmu.ds.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

class YamlReader {
	
	@SuppressWarnings("unchecked")
	protected ArrayList<Map<String, Object>> getConfig(String file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		
		Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>)
				yaml.load(new FileInputStream(new File(file)));
		return values.get("configuration");
	}

    @SuppressWarnings("unchecked")
    protected ArrayList<Rule> getRules(String file, String ruleType) throws FileNotFoundException {
        Yaml yaml = new Yaml();

        Map<String, ArrayList<Map<String, Object>>> values = (Map<String, ArrayList<Map<String, Object>>>) yaml
                .load(new FileInputStream(new File(file)));
        ArrayList<Map<String, Object>> list =  values.get(ruleType);
        if (list == null) return null;
        ArrayList<Rule> rules = new ArrayList<>();
        for (Map<String, Object> map: list) {
            Rule.Action act;
            switch ((String)map.get("action")) {
                case "drop": act = Rule.Action.DROP;
                    break;
                case "dropAfter": act = Rule.Action.DROPAFTER;
                    break;
                case "delay": act = Rule.Action.DELAY;
                    break;
                default:
                    act = Rule.Action.NOTSET;
            }
            Rule rule = new Rule(act);
            rule.dest = (String)map.get("dest");
            rule.src = (String)map.get("src");
            Integer seqNum = (Integer)map.get("seqNum");
            if (act == Rule.Action.DROPAFTER)
                rule.seqNum = seqNum;
            rules.add(rule);
        }
        return rules;
    }
	
}
