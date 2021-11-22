package org.dcsa.api.validator.util;

import com.fasterxml.jackson.databind.JsonNode;;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

public class JsonParser {
    Map<String, Object> jsonValues = new HashMap<>();

    public Map<String, Object> process(String prefix, JsonNode currentNode) {
        if (currentNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) currentNode;
            String[] tokens = prefix.split("\\.");
            if (arrayNode.isEmpty())
                jsonValues.put(tokens[tokens.length - 1], currentNode);
            else if (arrayNode.get(0).isTextual()) {
                jsonValues.put(tokens[tokens.length - 1], currentNode);
            } else {
                Iterator<JsonNode> node = arrayNode.elements();
                int index = 1;
                while (node.hasNext()) {
                    process(!prefix.isEmpty() ? prefix + ".[" + (index - 1) + "]" : "[" + String.valueOf(index - 1) + "", node.next());
                    index += 1;
                }
            }
        } else if (currentNode.isObject()) {
            currentNode.fields().forEachRemaining(entry -> process(!prefix.isEmpty() ? prefix + "." + entry.getKey() : entry.getKey(), entry.getValue()));
        } else {
            String[] tokens = prefix.split("\\.");
            jsonValues.put(tokens[tokens.length - 1], currentNode);
        }
        return jsonValues;
    }
}