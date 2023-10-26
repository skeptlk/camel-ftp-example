package camel.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;

public class KafkaPublisherRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

        // lets shutdown faster in case of in-flight messages stack up
        getContext().getShutdownStrategy().setTimeout(10);

        var csv = new CsvDataFormat().setUseMaps(true).setTrim(true);

        from("{{ftp.input}}")
            .choice()
                .when(simple("${header.CamelFileNameOnly}").endsWith(".csv"))
                .unmarshal(csv)
                .to("bean:fileTransformation")
                .to("kafka:{{producer.topic}}")
                .log("Writing to kafka!")
                .log("${headers}")
            .otherwise()
                .log("Wrong file in input directory: ${header.CamelFileNameOnly}")
                .to("{{ftp.fault}}" + "&fileName=${header.CamelFileNameOnly}");
    }

}
