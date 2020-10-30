package com.example.multipleoutput;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {RoutingKeyTestApplication.class, TestChannelBinderConfiguration.class})
class InputMessageProcessorTest {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private OutputDestination outputDestination;

    @RepeatedTest(10)
    public void testInputMessageProcessor() {

        inputDestination.send(MessageBuilder.withPayload(new InputMessage("abc")).build(), "myInputExchange");

        Message<byte[]> received = outputDestination.receive(0L, "myOutputExchange");
        assertThat(received).isNotNull();
    }
}