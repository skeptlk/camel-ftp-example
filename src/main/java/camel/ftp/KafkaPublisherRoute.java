package camel.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.dataformat.csv.CsvDataFormat;

public class KafkaPublisherRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");


        // lets shutdown faster in case of in-flight messages stack up
        getContext().getShutdownStrategy().setTimeout(10);

        var csv = new CsvDataFormat().setUseMaps(true);

        from("{{ftp.input}}")
            .choice()
                .when(simple("${header.CamelFileNameOnly}").endsWith(".csv"))
                .unmarshal(csv)
                .to("bean:fileTransformation")
                .marshal(csv.setSkipHeaderRecord(false))
                // push to Kafka
                .setHeader(KafkaConstants.PARTITION_KEY, simple("0"))
                .setHeader(KafkaConstants.KEY, simple("${header.CamelFileNameOnly}"))
                .to("kafka:{{producer.topic}}").log("${headers}")
            .otherwise()
                .log("Wrong file in input directory: ${header.CamelFileNameOnly}")
                .to("{{ftp.fault}}" + "&fileName=${header.CamelFileNameOnly}");
    }

}
