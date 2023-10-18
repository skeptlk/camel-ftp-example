package camel.ftp;


import org.apache.camel.component.jackson.ListJacksonDataFormat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class FileTransformation {
    public List<HashMap<String, String>> handler(List<List<String>> csvData) {
        var header = csvData.getFirst();
        var list = new LinkedList<HashMap<String, String>>();
        for (int i = 1; i < csvData.size(); i++) {
            var row = csvData.get(i);
            var map = new HashMap<String, String>();
            for (int j = 0; j < header.size(); j++) {
                map.put(header.get(j).trim(), row.get(j));
            }
            list.add(map);
        }
        return list;
    }
}
