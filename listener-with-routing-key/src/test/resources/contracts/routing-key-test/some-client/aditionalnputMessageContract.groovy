import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label "a producer with rabbitmq routing key"
    name "a producer with rabbitmq routing key"
    input {
        messageFrom("myInputExchange")
        messageHeaders {
            header("amqp_receivedRoutingKey", "additionalMessages")
        }
        messageBody([
                "aText": "just a text"
        ])
    }

    outputMessage {
        sentTo("myOutputExchange")
        body([
                "aProcessedText": $(anyNonBlankString())
        ])
//        headers {
//            header("amqp_receivedRoutingKey", "messages")
//        }
    }
}