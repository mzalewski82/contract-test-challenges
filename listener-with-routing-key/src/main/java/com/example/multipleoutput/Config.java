package com.example.multipleoutput;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class Config {

    @Bean
    public Function<InputMessage, OutputMessage> inputMessageProcessor() {
        return incoming -> new OutputMessage("processed input message: " + incoming.getText());
    }

    @Bean
    public Function<AdditionalInputMessage, AdditionalOutputMessage> additionalInputMessageProcessor() {
        return incoming -> new AdditionalOutputMessage("processed additional input message: " + incoming.getaText());
    }
}
