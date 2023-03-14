package mail.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.SneakyThrows;
import mail.storage.controller.MessageController;
import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static mail.storage.MailStorageTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class MessageControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;
    @Value("#{ 'http://localhost:' + '${server.port}' }")
    private String url;
    RestTemplate restTemplate;

    @Autowired
    MessageControllerTest(MessageController messageController, MessageRepository messageRepository) {
        this.mvc = MockMvcBuilders
                .standaloneSetup(messageController)
                .build();
        this.objectMapper = new ObjectMapper();
        this.messageRepository = messageRepository;
        SimpleModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void addMessage() {
        String messageJson = objectMapper.writeValueAsString(getMessageDto());
        var result = mvc.perform(post("/messages")
                        .contentType(APPLICATION_JSON)
                        .content(messageJson))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    void addMessageWithIncorrectAttachmentUrl() {
        MessageDto messageDto = getMessageDto();
        messageDto.setAttachmentUrl("some-url");
        String messageJson = objectMapper.writeValueAsString(messageDto);
        var result = mvc.perform(post("/messages")
                        .content(messageJson)
                        .contentType(APPLICATION_JSON))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    void updateMessage() {
        messageRepository.save(new Message(getMessageDto()));
        String updateMessageJson = objectMapper.writeValueAsString(getMessageDtoForUpdate());
        var result = mvc.perform(put("/messages/1")
                        .contentType(APPLICATION_JSON)
                        .content(updateMessageJson))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }


    @SneakyThrows
    @Test
    void getMessageByNumber() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(new Message(messageDto));
        var result = mvc.perform(get("/messages/1")
                        .contentType(APPLICATION_JSON))
                .andReturn();
        var entityJson = result.getResponse().getContentAsString();
        Message message = objectMapper.readValue(entityJson, Message.class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(messageDto.getNumber(), message.getNumber());
        assertEquals(messageDto.getType(), message.getType());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getSender(), message.getSender());
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
        assertEquals(messageDto.getTopic(), message.getTopic());
        assertNotNull(message.getId());
    }

    @SneakyThrows
    @Test
    void deleteMessageByNumber() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(new Message(messageDto));
        var result = mvc.perform(delete("/messages/1"))
                .andReturn();
        var message = messageRepository.findByNumber(1L);
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(message.isEmpty());
    }

    @Test
    @SneakyThrows
    void deleteNotExistingMessage() {
        try {
            restTemplate.delete(url + "/messages/1");
        } catch (HttpClientErrorException exception) {
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @SneakyThrows
    @Test
    void getMessagesByTopic() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(new Message(messageDto));
        var result = mvc.perform(get("/messages/topics/" + messageDto.getTopic()))
                .andReturn();
        Message[] messages = objectMapper.readValue(result.getResponse().getContentAsString(), Message[].class);
        Message firstMessage = messages[0];
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(messageDto.getTopic(), firstMessage.getTopic());
    }

    @SneakyThrows
    @Test
    void getMessagesByType() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(new Message(messageDto));
        var result = mvc.perform(get("/messages/types/" + messageDto.getType()))
                .andReturn();
        Message[] messages = objectMapper.readValue(result.getResponse().getContentAsString(), Message[].class);
        Message firstMessage = messages[0];
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(messageDto.getType(), firstMessage.getType());
    }

    @SneakyThrows
    @Test
    void getMessagesByDateRange() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(new Message(messageDto));
        DateRangeDto dateRangeDto = DateRangeDto
                .builder()
                .beginDate(getBeginDate())
                .endDate(getEndDate())
                .build();
        var dateRangeJson = objectMapper.writeValueAsString(dateRangeDto);
        var result = mvc.perform(get("/messages/date")
                        .contentType(APPLICATION_JSON)
                        .content(dateRangeJson))
                .andReturn();
        Message[] messages = objectMapper.readValue(result.getResponse().getContentAsString(), Message[].class);
        assertTrue(isMessageDateInRange(messages[0], dateRangeDto));
    }

    @SneakyThrows
    @Test
    void addMessageThatAlreadyExists() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(new Message(messageDto));
        try {
            restTemplate.postForObject(url + "/messages", messageDto, Object.class);
        } catch (HttpClientErrorException exception) {
            int code = exception.getStatusCode().value();
            assertEquals(400, code);
        }
    }

    @SneakyThrows
    @Test
    void updateMessageThatIsNotDraft() {
        MessageDto messageDto = getMessageDto();
        messageDto.setType(MessageType.MAIN);
        messageRepository.save(new Message(messageDto));
        try {
            restTemplate.put(url + "/messages/1", getUpdateMessageDtoRequestEntity(), String.class);
        } catch (HttpClientErrorException exception) {
            assertEquals(400, exception.getStatusCode().value());
        }
    }

    HttpEntity<UpdateMessageDto> getUpdateMessageDtoRequestEntity() {
        var updateMessageDto = getMessageDtoForUpdate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new HttpEntity<>(updateMessageDto, headers);
    }

    @SneakyThrows
    @Test
    void getMessageByNotExistingNumber() {
        try {
            restTemplate.getForObject(url + "/messages/1", Object.class);
        } catch (HttpClientErrorException exception) {
            assertEquals(404, exception.getStatusCode().value());
        }
    }
}
