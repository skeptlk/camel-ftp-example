package camel.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class FtpRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

        // lets shutdown faster in case of in-flight messages stack up
        getContext().getShutdownStrategy().setTimeout(10);

        from("{{ftp.input}}")
                .unmarshal().csv()
                .to("bean:fileTransformation")
                .marshal().json(JsonLibrary.Jackson)
                .process(e -> {
                    String fileName = e.getIn().getHeader("CamelFileNameOnly").toString();
                    fileName = fileName.replace(".csv", ".test.json");
                    e.getIn().setHeader("fileName", fileName);
                })
//                .setHeader("fileName", header("CamelFileNameOnly").method(""))
                .log("${body}")
                .to("{{ftp.output}}" + "&fileName=${header.fileName}.json");
    }
}
