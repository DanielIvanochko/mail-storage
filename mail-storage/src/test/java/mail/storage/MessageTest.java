package mail.storage;

import mail.storage.domain.Message;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static mail.storage.MailStorageTestUtils.getMessageDto;
import static mail.storage.MailStorageTestUtils.getMessageDtoForUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MessageTest {
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
