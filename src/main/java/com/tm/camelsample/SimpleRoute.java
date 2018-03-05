package com.tm.camelsample;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRoute.class);

    @Override
    public void configure() {
        from("direct:my-inbound").routeId("my-route")
                .log("RECEIVED ${body}")
                .process(loggingProcessor()).to("direct:my-outbound");
    }

    private Processor loggingProcessor() {
        return exchange -> LOGGER.info("PROCESSING " + exchange.getIn().getBody());
    }
}
