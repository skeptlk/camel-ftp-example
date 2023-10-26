package camel.ftp;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * A basic example running as public static void main.
 */
public final class CamelApplication {
    private CamelApplication() {
    }

    public static void main(String[] args) throws Exception {
        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.getPropertiesComponent().setLocation("classpath:ftp.properties");

            setUpKafkaComponent(camelContext);

            DataSource postgresDS = setupPostgresDataSource("jdbc:postgresql://localhost:5432/postgres");

            camelContext.getRegistry().bind("postgres", postgresDS);

            camelContext.getRegistry().bind("fileTransformation", FileTransformation.class);

            camelContext.addRoutes(new KafkaPublisherRoute());
            camelContext.addRoutes(new KafkaConsumerRoute());

            camelContext.start();

            Thread.sleep(5L * 60 * 1000);
        }
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

    private static DataSource setupPostgresDataSource(String connectURI) {
        return new BasicDataSource() {{
            setDriverClassName("org.postgresql.Driver");
            setUsername("foo");
            setPassword("pass");
            setUrl(connectURI);
        }};
    }


}
