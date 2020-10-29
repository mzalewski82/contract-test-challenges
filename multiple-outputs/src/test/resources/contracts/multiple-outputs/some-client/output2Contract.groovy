import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label "producer with multiple outputs 2"
    name "producer with multiple outputs 2"
    input {
        messageFrom("myInputExchange")
        messageBody([
                "text": "just a text"
        ])
    }
    outputMessage {
        sentTo("myOutputExchange2")
        body([
                "aProcessedText": $(anyNonBlankString())
        ])
    }
}