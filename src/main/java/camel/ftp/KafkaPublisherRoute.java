package camel.ftp;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.camel.dataformat.csv.CsvDataFormat;

import java.util.HashMap;

public class KafkaPublisherRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");
        // lets shutdown faster in case of in-flight messages stack up
        getContext().getShutdownStrategy().setTimeout(10);

        setUpKafkaComponent(getContext());

        var csv = new CsvDataFormat().setUseMaps(true).setTrim(true);

        from("{{ftp.input}}")
            .choice()
                .when(simple("${header.CamelFileNameOnly}").endsWith(".csv"))
                .unmarshal(csv)
                .to("bean:fileTransformation")
                .to("kafka:{{producer.topic}}")
                .log("Writing csv to kafka!")
            .otherwise()
                .log("Wrong file in input directory: ${header.CamelFileNameOnly}")
                .to("{{ftp.fault}}" + "&fileName=${header.CamelFileNameOnly}");
    }

    static void setUpKafkaComponent(CamelContext camelContext) {
        HashMap<String, Object> additionalProps = new HashMap<>() {{
            put("schema.registry.url", "http://localhost:8081");
        }};

        // setup kafka component with the brokers
        ComponentsBuilderFactory.kafka()
                .brokers("{{kafka.brokers}}")
                .groupId("{{consumer.group}}")
                .keySerializer("io.confluent.kafka.serializers.KafkaAvroSerializer")
                .valueSerializer("io.confluent.kafka.serializers.KafkaAvroSerializer")
                .keyDeserializer("io.confluent.kafka.serializers.KafkaAvroDeserializer")
                .valueDeserializer("io.confluent.kafka.serializers.KafkaAvroDeserializer")
                .additionalProperties(additionalProps)
                .register(camelContext, "kafka");
    }

}
