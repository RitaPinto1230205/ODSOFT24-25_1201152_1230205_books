package pt.psoft.g1.psoftg1.bookmanagement.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import org.springframework.amqp.core.Message;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRabbitmqController {

    @Autowired
    private final BookService bookService;

    @RabbitListener(queues = "#{autoDeleteQueue_Book_Created.name}")
    public void receiveBookCreatedMsg(Message msg) {

        try {
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            BookViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, BookViewAMQP.class);

            System.out.println(" [x] Received Book Created by AMQP: " + msg + ".");
            try {
                bookService.create(bookViewAMQP);
                System.out.println(" [x] New book inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Book already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving book event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Book_Updated.name}")
    public void receiveBookUpdated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, BookViewAMQP.class);

            System.out.println(" [x] Received Book Updated by AMQP: " + msg + ".");
            try {
                bookService.update(bookViewAMQP);
                System.out.println(" [x] Book updated from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Book does not exists or wrong version. Nothing stored.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving book event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Book_Photo_Removed.name}")
    public void receiveBookPhotoRemoved(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(jsonReceived);

            String isbn = jsonNode.get("isbn").asText();
            long version = jsonNode.get("version").asLong();

            System.out.println(" [x] Received Book Photo Remove request by AMQP: " + msg + ".");
            try {
                bookService.removeBookPhoto(isbn, version);
                System.out.println(" [x] Book photo removed from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Could not remove book photo: " + e.getMessage());
            }
        } catch(Exception ex) {
            System.out.println(" [x] Exception receiving book event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Book_Search.name}")
    public void receiveBookSearch(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(jsonReceived);

            SearchBooksQuery query = new SearchBooksQuery();
            query.setTitle(jsonNode.get("title").asText());
            query.setGenre(jsonNode.get("genre").asText());

            Page page = new Page(
                    jsonNode.get("number").asInt(),
                    jsonNode.get("limit").asInt()
            );

            System.out.println(" [x] Received Book Search request by AMQP: " + msg + ".");
            try {
                List<Book> books = bookService.searchBooks(page, query);
                System.out.println(" [x] Book search completed from AMQP: found " + books.size() + " books.");
            } catch (Exception e) {
                System.out.println(" [x] Could not perform book search: " + e.getMessage());
            }
        } catch(Exception ex) {
            System.out.println(" [x] Exception receiving book event from AMQP: '" + ex.getMessage() + "'");
        }
    }
}