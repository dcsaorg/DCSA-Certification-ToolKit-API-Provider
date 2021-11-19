package org.dcsa.api.validator.util;

import com.fasterxml.jackson.databind.JsonNode;;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

public class JsonParser {

    private List<String> pathList;
    private Map<String,String> dataTypes=new HashMap<>();
    public static void process(String prefix, JsonNode currentNode) {
        if (currentNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) currentNode;
            Iterator<JsonNode> node = arrayNode.elements();
            int index = 1;
            while (node.hasNext()) {
                process(!prefix.isEmpty() ? prefix + ".[" + (index-1) +"]": "["+String.valueOf(index-1)+"", node.next());
                index += 1;
            }
        }
        else if (currentNode.isObject()) {
            currentNode.fields().forEachRemaining(entry -> process(!prefix.isEmpty() ? prefix + "." + entry.getKey() : entry.getKey(), entry.getValue()));
        }
        else {
            String text=prefix + ": " + currentNode.toString();
            System.out.println("Json path:"+text);
            if(text.contains(".type: "+ currentNode.toString())) {
                String[] tokens= text.split(".");
                System.out.println(tokens[tokens.length-2] + ": " + currentNode.toString());
            }
            else if(text.contains(".type.[") && !(currentNode.toString().equals("\"null\"")) && !(currentNode.toString().equals("null"))) {
                String[] tokens= text.split(".");
                System.out.println(tokens[tokens.length-3] + ": " + currentNode.toString());
            }

        }
    }
}