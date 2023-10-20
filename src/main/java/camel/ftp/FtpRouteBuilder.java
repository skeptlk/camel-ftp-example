package camel.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;


public class FtpRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // configure properties component
        getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

        // lets shutdown faster in case of in-flight messages stack up
        getContext().getShutdownStrategy().setTimeout(10);

        var csv = new CsvDataFormat().setUseMaps(true);

        from("{{ftp.input}}")
            .choice()
                .when(simple("${header.CamelFileNameOnly}").endsWith(".csv"))
                .unmarshal(csv)
                .to("bean:fileTransformation")
                .marshal(csv.setSkipHeaderRecord(false))
                .process(new HeaderProcessor())
                .to("{{ftp.output}}" + "&fileName=${header.fileName}")
            .otherwise()
                .to("{{ftp.fault}}" + "&fileName=${header.CamelFileNameOnly}");
    }
}
