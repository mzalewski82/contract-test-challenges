import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label "producer with multiple outputs 1"
    name "producer with multiple outputs 1"
    input {
        messageFrom("myInputExchange")
        messageBody([
                "text": "just a text"
        ])
    }
//    nie widzę możliwości by sprawdzić kilka wiadomości na wyjściu.
    outputMessage {
        sentTo("myOutputExchange1")
        body([
                "processedText": $(anyNonBlankString())
        ])
    }
}