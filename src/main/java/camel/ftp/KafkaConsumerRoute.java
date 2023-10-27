package camel.ftp;

import org.apache.avro.generic.GenericRecord;
import org.apache.camel.builder.RouteBuilder;

public class KafkaConsumerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");
        getContext().getShutdownStrategy().setTimeout(10);

        from("kafka:{{consumer.topic}}")
                .log("Consumed message, ${body.get(\"flight\")}")
                .process(exchange -> {
                    var body = exchange.getMessage().getBody(GenericRecord.class);
                    var query = String.format(
                            "insert into schedule (flight, company, time, arr, dep) values ('%s', '%s', '%s', '%s', '%s');",
                            body.get("flight"),
                            body.get("company"),
                            body.get("time"),
                            body.get("arr"),
                            body.get("dep"));
                    exchange.getMessage().setBody(query);
                })
        .to("jdbc:postgres")
        .log("Pushed to postgres");
        }
}
