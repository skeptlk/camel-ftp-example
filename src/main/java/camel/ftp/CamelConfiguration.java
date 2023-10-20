package camel.ftp;

import org.apache.camel.BindToRegistry;
import org.apache.camel.Configuration;

@Configuration
public class CamelConfiguration {
    @BindToRegistry
    public FileTransformation fileTransformation() {
        return new FileTransformation();
    }
}
