package fr.inria.diversify.sosie.compare.stackElement;

import java.util.*;

/**
 * Created by Simon on 24/04/14.
 */
public class StackTraceVariable extends StackTraceElement {
    protected Map<String,Object> vars;
    protected int id;


    public StackTraceVariable(String value, int deep, Map<String, String> idMap) {
        originalDeep = deep;
        String[] tmp = value.split(":;:");
        String[] idTmp = tmp[0].split(";");
        id = Integer.parseInt(idTmp[0]);
        method = idMap.get(idTmp[1]);

        vars = new HashMap<>();
        if(tmp[1].equals("P"))
            return;

        for(int i = 1; i < tmp.length; i++ ) {
            String[] varTmp = tmp[i].split(";");
            if(varTmp.length == 1)
                vars.put(idMap.get(varTmp[0]), "");
            else
                vars.put(idMap.get(varTmp[0]), parseValue(varTmp[1]));
        }
    }

    public Map<String,Object> getVariables() {return vars;}

    protected Object parseValue(String valueString) {
        if(valueString.startsWith("{") && valueString.endsWith("}")) {
            Set<Object> set = new HashSet<>();
            for(String s : valueString.substring(1,valueString.length()-1).split(", "))
                set.add(parseValue(s));
            return set;
        }

        if(valueString.startsWith("[") && valueString.endsWith("]")) {
            List<Object> list = new ArrayList<>();
            for(String s : valueString.substring(1,valueString.length()-1).split(", "))
                list.add(parseValue(s));
            return list;
        }

        if(valueString.contains("@") && valueString.split("@").length != 0)
            return valueString.split("@")[0];

        return valueString;
    }
}