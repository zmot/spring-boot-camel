package com.tm.camelsample;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@MockEndpointsAndSkip("direct:my-outbound*")

public class RouteTest {
    @Produce(uri = "direct:my-inbound")
    ProducerTemplate in;

    @EndpointInject(uri = "mock:direct:my-outbound")
    MockEndpoint out;

    @Test
    public void shouldProcessMessage() throws InterruptedException {
        out.expectedBodiesReceived("test body content");

        in.sendBody("test body content");

        out.assertIsSatisfied();
    }
}