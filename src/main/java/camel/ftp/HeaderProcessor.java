package camel.ftp;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.text.SimpleDateFormat;
import java.util.Date;

class HeaderProcessor implements Processor {
    @Override
    public void process(Exchange e) throws Exception {
        String fileName = e.getIn()
                .getHeader("CamelFileNameOnly", String.class)
                .replace(".csv", "");

        String[] fileNameSplit = fileName.split("-");

        if (fileNameSplit.length != 2) {
            e.getIn().setHeader("fileName", fileName + ".csv");
            return;
        }

        SimpleDateFormat inputFormat  = new SimpleDateFormat("dd.MM.yyyy"),
                         outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = inputFormat.parse(fileNameSplit[1]);

        e.getIn().setHeader("fileName", String.format("invoice.%s.csv", outputFormat.format(date)));
    }
}
