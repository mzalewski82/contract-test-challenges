package com.example.multipleoutput;

import com.example.multipleoutput.routing_key_test.Some_clientTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

import javax.inject.Inject;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.messaging.util.ContractVerifierMessagingUtil.headers;

/**
 * Kopia testu wygenerowanego z kontraktu.
 * @see Some_clientTest
 * Dodałem @RepeatedTest(10) by lepiej było widać problem z routing key.
 * Stub ze Spring Cloud Stream
 * @see TestChannelBinderConfiguration
 * Jest jeden dla Kafki i RabbitMQ, nie obsługuje AMQP routing key (tak mi sie przynajmniej wydaje).
 * Z tego powodu wiadomości lecą do niewłaściwego konsumenta, losowego podpiętego pod ten sam echange.
 * Da się to przetestować bez fizycznego RabbitMQ? W pamieci, konterze, jakiegokolwiek.
 */
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
