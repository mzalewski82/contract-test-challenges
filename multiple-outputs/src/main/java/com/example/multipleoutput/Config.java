package com.example.multipleoutput;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Function;

@Configuration
public class Config {

    @Bean
    public Function<Flux<InputMessage>, Tuple2<Flux<OutputMessage>, Flux<AdditionalOutputMessage>>> inputMessageProcessor() {
        return flux -> {
            Flux<InputMessage> connectedFlux = flux.publish().autoConnect(2);
            UnicastProcessor<OutputMessage> outputMessagesProcessor = UnicastProcessor.create();
            UnicastProcessor<AdditionalOutputMessage> additionalOutputMessagesProcessor = UnicastProcessor.create();
            Flux<OutputMessage> outputMessageFlux = connectedFlux.map(incoming -> new OutputMessage("processed input message: " + incoming.getText()))
                    .doOnNext(outputMessagesProcessor::onNext);
            Flux<AdditionalOutputMessage> additionalOutputMessageFlux = connectedFlux.map(incoming -> new AdditionalOutputMessage("processed additional input message: " + incoming.getText()))
                    .doOnNext(additionalOutputMessagesProcessor::onNext);

            return Tuples.of(outputMessageFlux, additionalOutputMessageFlux);

        };
    }
}
