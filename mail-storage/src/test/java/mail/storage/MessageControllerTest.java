package mail.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mail.storage.controller.MessageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static mail.storage.MailStorageTestUtils.getMessageDto;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageControllerTest(MessageController messageController) {
        this.mvc = MockMvcBuilders
                .standaloneSetup(messageController)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void addMessageTest() {
        String messageJson = null;
        try {
            messageJson = objectMapper.writeValueAsString(getMessageDto());
            var result = mvc.perform(post("/message")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(messageJson))
                    .andReturn();
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
