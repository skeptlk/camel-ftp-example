package camel.ftp;

import camel.ftp.model.FlightRecord;
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
                var flightRecord = new FlightRecord(hashMap);
                var parser = new Schema.Parser();

                Schema schema = null;
                try {
                    var file = getClass().getClassLoader().getResourceAsStream("schema/record.avsc");
                    schema = parser.parse(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                var record = new GenericData.Record(schema);
                record.put("flight", flightRecord.flight);
                record.put("time", flightRecord.time);
                record.put("company", flightRecord.company);
                record.put("arr", flightRecord.arr);
                record.put("dep", flightRecord.dep);
                return record;
            })
            .toList();
    }
}
