package camel.ftp;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CsvAggregationStrategy implements AggregationStrategy {
    private static final String SEPARATOR = "\n";

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);
        if (oldBody.isBlank()) {
            return newExchange; // header row
        }
        oldExchange.getIn().setBody(oldBody + SEPARATOR + newBody);
        return oldExchange;
    }
}
