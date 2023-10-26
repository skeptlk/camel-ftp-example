package camel.ftp;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FileTransformation {
    public List<GenericData.Record> handler(List<HashMap<String, String>> csvData) {
        return csvData
            .stream()
            //.filter(map -> Objects.equals(map.get("company"), "s7"))
            .map(hashMap -> {
                var parser = new Schema.Parser();

                Schema schema;
                try {
                    var file = getClass().getClassLoader().getResourceAsStream("schema/record.avsc");
                    schema = parser.parse(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                var record = new GenericData.Record(schema);
                record.put("flight", hashMap.get("flight"));
                record.put("time", hashMap.get("time"));
                record.put("company", hashMap.get("company"));
                record.put("arr", hashMap.get("arr"));
                record.put("dep", hashMap.get("dep"));
                return record;
            })
            .toList();
    }
}
