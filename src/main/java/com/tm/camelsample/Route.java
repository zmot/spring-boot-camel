package com.tm.camelsample;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Route.class);

    @Override
    public void configure() {
        from("direct:my-inbound").routeId("my-route")
                .log("RECEIVED ${body}")
                .process(exchange -> {
                    LOGGER.info("PROCESSING " + exchange.getIn().getBody());
                }).to("direct:my-outbound");
    }
}
