package CDC.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

import org.springframework.amqp.core.MessageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookRabbitmqController;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        , classes = {BookRabbitmqController.class, BookService.class}
)
@PactConsumerTest
@PactTestFor(providerName = "book_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class BooksCDCDefinitionTest {

    @MockBean
    BookService bookService;

    @Autowired
    BookRabbitmqController listener;

    @Pact(consumer = "book_created-consumer")
    V4Pact createBookCreatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("isbn", "6475803429671");
        body.stringType("title", "title");
        body.stringType("description", "description");
        body.stringType("genre", "Infantil");
        body.array("authorIds")
                .integerType(1)
                .closeArray();
        body.stringMatcher("version", "[0-9]+", "1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a book created event").withMetadata(metadata).withContent(body).toPact();
    }

    @Pact(consumer = "book_updated-consumer")
    V4Pact createBookUpdatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("isbn", "6475803429671")
                .stringType("title", "updated title")
                .stringType("description", "description")
                .stringType("genre", "Infantil");
        body.array("authorIds")
                .integerType(1)
                .closeArray();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a book updated event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    //
// The following tests are now defined as IT tests, so that the definition of contract and the tests are decoupled.
// Yet, while the body of the tests can be elsewhere, the method signature must be defined here so the contract is generated.
//
    @Test
    @PactTestFor(pactMethod = "createBookCreatedPact")
    void testBookCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            listener.receiveBookCreatedMsg(message);
        });

        verify(bookService, times(1)).create(any(BookViewAMQP.class));
    }

    @Test
    @PactTestFor(pactMethod = "createBookUpdatedPact")
    void testBookUpdated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            listener.receiveBookUpdated(message);
        });

        // Verify interactions with the mocked service
        verify(bookService, times(1)).update(any(BookViewAMQP.class));
    }


    @Pact(consumer = "book_photo_removed-consumer")
    V4Pact createBookPhotoRemovedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("isbn", "6475803429671")
                .numberType("version", 1L);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a book photo removed event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createBookPhotoRemovedPact")
    void testBookPhotoRemoved(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            listener.receiveBookPhotoRemoved(message);
        });

        verify(bookService, times(1)).removeBookPhoto(any(String.class), anyLong());
    }


    @Pact(consumer = "book_search-consumer")
    V4Pact createBookSearchPact(MessagePactBuilder builder) {
        PactDslJsonBody searchBody = new PactDslJsonBody()
                .stringType("title", "Test Book")
                .stringType("genre", "Genre 1")
                .numberType("number", 1)
                .numberType("limit", 10);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a book search event")
                .withMetadata(metadata)
                .withContent(searchBody)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createBookSearchPact")
    void testBookSearch(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();
        System.out.println("Test JSON: " + jsonReceived);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            listener.receiveBookSearch(message);
        });

        verify(bookService, times(1)).searchBooks(
                argThat(page ->
                        page.getNumber() == 1 &&
                                page.getLimit() == 10
                ),
                argThat(query ->
                        query.getTitle().equals("Test Book") &&
                                query.getGenre().equals("Genre 1")
                )
        );
    }
}