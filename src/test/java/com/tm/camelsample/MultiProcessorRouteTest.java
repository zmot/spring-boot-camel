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

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@MockEndpointsAndSkip("direct:my-outbound*")
public class MultiProcessorRouteTest {
    @Produce
    private ProducerTemplate in;

    @EndpointInject(uri = "mock:direct:my-outbound-fast")
    private MockEndpoint outFast;

    @EndpointInject(uri = "mock:direct:my-outbound-slow")
    private MockEndpoint outSlow;

    @Test
    public void shouldUseMultipleProcessorsAndOutbounds() throws InterruptedException {
        //given
        int messageCount = 6;
        long endpointTimeout = TimeUnit.SECONDS.toMillis(10);
        outFast.expectedMessageCount(messageCount / 2);
        outSlow.expectedMessageCount(messageCount / 2);

        //when
        IntStream.range(0, messageCount)
                .forEach(i -> in.asyncSendBody("direct:my-proc-inbound", String.valueOf(i)));

        //then
        outFast.assertIsSatisfied(endpointTimeout);
        outSlow.assertIsSatisfied(endpointTimeout);
    }
}