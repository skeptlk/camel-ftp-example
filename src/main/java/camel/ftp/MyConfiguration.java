package camel.ftp;

import org.apache.camel.BindToRegistry;
import org.apache.camel.CamelContext;
import org.apache.camel.Configuration;
import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class MyConfiguration {
    @BindToRegistry
    public FileTransformation fileTransformation() {
        return  new FileTransformation();
    }


    @BindToRegistry
    public DataSource postgres() {
        return new BasicDataSource() {{
            setDriverClassName("org.postgresql.Driver");
            setUsername("foo");
            setPassword("pass");
            setUrl("jdbc:postgresql://localhost:5432/postgres");
        }};
    }

}
