package org.cocoine.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CoCoIne {

    /**
     * Used to read the locator files, configuration and test data
     * @param filePath
     * @param key
     * @return
     * @throws IOException
     */
    public Iterator getJsonIterator(String filePath, String key) throws IOException {
        InputStream inputStream = null;
        ClassLoader classLoader = getClass().getClassLoader();
        inputStream = classLoader.getResourceAsStream(filePath);
        if (inputStream == null) {
            return null;
        }
        HashMap<String, Object> result =
                new ObjectMapper().readValue(inputStream, HashMap.class);
        Collection<Object[]> collection = new ArrayList<Object[]>();
        List<Map<String, String>> jsonMap = (ArrayList) result.get(key);
        for (Map<String, String> map : jsonMap) {
            collection.add(new Object[]{map});
        }
        return collection.iterator();
    }
}
