package camel.ftp;

import org.apache.camel.builder.RouteBuilder;

public class KafkaConsumerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

        from("kafka:{{consumer.topic}}").log("Consumed message::: ${body}");
    }
}
