package camel.ftp;

import org.apache.camel.builder.RouteBuilder;

public class KafkaConsumerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

        getContext().getShutdownStrategy().setTimeout(10);

        from("kafka:{{consumer.topic}}").log("Consumed message::: ${body}");
    }
}
