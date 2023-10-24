package camel.ftp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public final class FlightRecord {
    @JsonProperty
    public final String flight;
    @JsonProperty
    public final String company;
    @JsonProperty
    public final String time;
    @JsonProperty
    public final String arr;
    @JsonProperty
    public final String dep;

    public FlightRecord(
            String flight, String company, String time, String arr, String dep) {
        this.flight = flight;
        this.company = company;
        this.time = time;
        this.arr = arr;
        this.dep = dep;
    }

    public FlightRecord(Map<String, String> map) {
        this(
                map.get("flight"),
                map.get("company"),
                map.get("time"),
                map.get("arr"),
                map.get("dep")
        );
    }



}
