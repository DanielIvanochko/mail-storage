package mail.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mail.storage.controller.MessageController;
import mail.storage.domain.Message;
import mail.storage.domain.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.repository.MessageRepository;
import mail.storage.util.MessageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static mail.storage.MailStorageTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;

    @Autowired
    MessageControllerTest(MessageController messageController, MessageRepository messageRepository) {
        this.mvc = MockMvcBuilders
                .standaloneSetup(messageController)
                .build();
        this.objectMapper = new ObjectMapper();
        this.messageRepository = messageRepository;
    }

    @BeforeEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void addMessageTest() {
        String messageJson = objectMapper.writeValueAsString(getMessageDto());
        var result = mvc.perform(post("/message")
                        .contentType(APPLICATION_JSON)
                        .content(messageJson))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    void updateMessageTest() {
        messageRepository.save(MessageUtils.getMessageFromDto(getMessageDto()));
        String updateMessageJson = objectMapper.writeValueAsString(getMessageDtoForUpdate());
        var result = mvc.perform(put("/message/1")
                        .contentType(APPLICATION_JSON)
                        .content(updateMessageJson))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }


    @SneakyThrows
    @Test
    void getMessageByNumber() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        var result = mvc.perform(get("/message/1")
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
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        var result = mvc.perform(delete("/message/1"))
                .andReturn();
        var message = messageRepository.findByNumber(1L);
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(message.isEmpty());
    }

    @SneakyThrows
    @Test
    void getMessagesByTopic() {
        MessageDto messageDto = getMessageDto();
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        var result = mvc.perform(get("/message/topic?name=" + messageDto.getTopic()))
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
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        var result = mvc.perform(get("/message/type?name=" + messageDto.getType()))
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
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        DateRangeDto dateRangeDto = DateRangeDto
                .builder()
                .beginDate(getBeginDate())
                .endDate(getEndDate())
                .build();
        var dateRangeJson = objectMapper.writeValueAsString(dateRangeDto);
        var result = mvc.perform(get("/message/date")
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
        String entityJson = objectMapper.writeValueAsString(messageDto);
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        var result = mvc.perform(post("/message")
                        .contentType(APPLICATION_JSON)
                        .content(entityJson))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    void updateMessageThatIsNotDraft() {
        MessageDto messageDto = getMessageDto();
        messageDto.setType(MessageType.MAIN);
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        var updateMessageDto = getMessageDtoForUpdate();
        String entityJson = objectMapper.writeValueAsString(updateMessageDto);
        var result = mvc.perform(put("/message/1")
                        .contentType(APPLICATION_JSON)
                        .content(entityJson))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    void getMessageByNotExistingNumber() {
        var result = mvc.perform(get("/message/1"))
                .andReturn();
        assertEquals(404, result.getResponse().getStatus());
    }
}
