package com.tm.camelsample;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MultiProcessorRoute extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiProcessorRoute.class);

    @Override
    public void configure() {
        from("direct:my-proc-inbound").routeId("my-multi-processor-route")
                .multicast().parallelProcessing()
                .to("direct:slow-processing", "direct:fast-processing");

        from("direct:fast-processing").routeId("my-fast-route")
                .filter(odd())
                .process(fastProcessor())
                .to("direct:my-outbound-fast");

        from("direct:slow-processing").routeId("my-slow-route")
                .filter(even())
                .process(slowProcessor())
                .to("direct:my-outbound-slow");
    }

    private Processor slowProcessor() {
        return createWaitingProcessor(1000, "SLOW");
    }

    private Processor fastProcessor() {
        return createWaitingProcessor(100, "FAST");
    }

    private Processor createWaitingProcessor(int waitTime, String type) {
        return exchange -> {
            LOGGER.info(type + " PROCESSING " + exchange.getIn().getBody());
            Thread.sleep(waitTime);
        };
    }

    private Predicate even() {
        return createIntModuloFilter(0);
    }

    private Predicate odd() {
        return createIntModuloFilter(1);
    }

    private Predicate createIntModuloFilter(int remainder) {
        return exchange -> exchange.getIn().getBody(Integer.class) % 2 == remainder;
    }
}
