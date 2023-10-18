package camel.ftp;

import org.apache.camel.main.Main;

/**
 * A basic example running as public static void main.
 */
public final class CamelApplication {
    private CamelApplication() {
    }

    public static void main(String[] args) throws Exception {
        // use Camels Main class
        Main main = new Main(CamelApplication.class);
        // now keep the application running until the JVM is terminated (ctrl + c or sigterm)
        main.run(args);
    }
}
