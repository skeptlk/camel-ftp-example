package camel.ftp;

import org.apache.camel.main.Main;

public class MyApplication {
    public static void main(String[] args) throws Exception {
        // use Camels Main class
        Main main = new Main(MyApplication.class);
        // now keep the application running until the JVM is terminated (ctrl + c or sigterm)
        main.run(args);
    }
}
