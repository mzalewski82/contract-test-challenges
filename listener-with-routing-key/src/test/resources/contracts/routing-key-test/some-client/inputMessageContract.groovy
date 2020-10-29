import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label "producer with rabbitmq routing key"
    name "producer with rabbitmq routing key"
    input {
        messageFrom("myInputExchange")
        messageHeaders {
            header("amqp_receivedRoutingKey", "messages")
        }
        messageBody([
                "text": "just a text"
        ])
    }

    outputMessage {
        sentTo("myOutputExchange")
        body([
                "processedText": $(anyNonBlankString())
        ])
//        headers {
//            header("amqp_receivedRoutingKey", "messages")
//        }
    }
}