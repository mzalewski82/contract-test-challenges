package com.example.multipleoutput;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;

import javax.inject.Inject;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.messaging.util.ContractVerifierMessagingUtil.headers;

@SuppressWarnings("rawtypes")
public class CustomSome_clientTest extends MessagingContractBase {
	@Inject ContractVerifierMessaging contractVerifierMessaging;
	@Inject ContractVerifierObjectMapper contractVerifierObjectMapper;

	@RepeatedTest(10)
	public void validate_a_producer_with_rabbitmq_routing_key() throws Exception {
		// given:
			ContractVerifierMessage inputMessage = contractVerifierMessaging.create(
					"{\"aText\":\"just a text\"}"
						, headers()
							.header("amqp_receivedRoutingKey", "additionalMessages")
			);

		// when:
			contractVerifierMessaging.send(inputMessage, "myInputExchange");

		// then:
			ContractVerifierMessage response = contractVerifierMessaging.receive("myOutputExchange");
			assertThat(response).isNotNull();

		// and:
			DocumentContext parsedJson = JsonPath.parse(contractVerifierObjectMapper.writeValueAsString(response.getPayload()));
			assertThatJson(parsedJson).field("['aProcessedText']").matches("^\\s*\\S[\\S\\s]*");
	}

	@RepeatedTest(10)
	public void validate_producer_with_rabbitmq_routing_key() throws Exception {
		// given:
			ContractVerifierMessage inputMessage = contractVerifierMessaging.create(
					"{\"text\":\"just a text\"}"
						, headers()
							.header("amqp_receivedRoutingKey", "messages")
			);

		// when:
			contractVerifierMessaging.send(inputMessage, "myInputExchange");

		// then:
			ContractVerifierMessage response = contractVerifierMessaging.receive("myOutputExchange");
			assertThat(response).isNotNull();

		// and:
			DocumentContext parsedJson = JsonPath.parse(contractVerifierObjectMapper.writeValueAsString(response.getPayload()));
			assertThatJson(parsedJson).field("['processedText']").matches("^\\s*\\S[\\S\\s]*");
	}

}
