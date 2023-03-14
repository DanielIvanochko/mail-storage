package mail.storage;

import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static mail.storage.MailStorageTestUtils.getMessageDto;
import static mail.storage.MailStorageTestUtils.getMessageDtoForUpdate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageTest {
    @Test
    void messageDtoConstructorTest() {
        MessageDto messageDto = getMessageDto();
        Message message = new Message(messageDto);
        assertEquals(messageDto.getNumber(), message.getNumber());
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getTopic(), message.getTopic());
        assertEquals(messageDto.getType(), message.getType());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getSender(), message.getSender());
    }

    @Test
    void messageBuilderTest() {
        Message message = Message.builder()
                .body("body")
                .topic("topic")
                .id("id")
                .type(MessageType.MAIN)
                .attachmentUrl("http://localhost")
                .date(LocalDateTime.now())
                .receiver("me@gmail.com")
                .sender("you@gmail.com")
                .number(1L)
                .build();
        assertEquals("body", message.getBody());
        assertEquals("id", message.getId());
        assertEquals("http://localhost", message.getAttachmentUrl());
        assertEquals("topic", message.getTopic());
        assertEquals(MessageType.MAIN, message.getType());
        assertEquals(1L, message.getNumber());
        assertEquals("me@gmail.com", message.getReceiver());
        assertEquals("you@gmail.com", message.getSender());
    }

    @Test
    void equalsTest() {
        Message first = new Message(getMessageDto());
        Message second = new Message(getMessageDto());
        assertEquals(first, second);
    }

    @Test
    void equalsNegativeTest() {
        Message first = new Message(getMessageDto());
        Message second = new Message(getMessageDto());
        first.setType(MessageType.MAIN);
        second.setSender("hello@gmail.com");
        assertNotEquals(first, second);
    }

    @Test
    void isDraftNegativeTest() {
        Message message = new Message(getMessageDto());
        message.setType(MessageType.MAIN);
        assertFalse(message.isDraft());
        message.setType(null);
        assertFalse(message.isDraft());
    }

    @Test
    void updateMessageWithDtoTest() {
        UpdateMessageDto messageDto = getMessageDtoForUpdate();
        Message message = new Message();
        message.updateWithDto(messageDto);
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getTopic(), message.getTopic());
        assertEquals(messageDto.getType(), message.getType());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getSender(), message.getSender());
    }

    @Test
    void isDraftTest() {
        MessageDto messageDto = getMessageDto();
        Message message = new Message(messageDto);
        assertTrue(message.isDraft());
    }
}
