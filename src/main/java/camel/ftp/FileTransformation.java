package camel.ftp;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileTransformation {
    public List<HashMap<String, String>> handler(List<HashMap<String, String>> csvData) {
        return csvData
                .stream()
                .filter(stringStringHashMap -> Objects.equals(stringStringHashMap.get("company"), "s7"))
                .toList();
    }
}
