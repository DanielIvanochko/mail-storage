package mail.storage;

import lombok.SneakyThrows;
import mail.storage.domain.Message;
import mail.storage.dto.MessageDto;
import mail.storage.repository.MessageRepository;
import mail.storage.service.MailStorageService;
import mail.storage.util.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MailStorageServiceTest {
    private final MessageRepository messageRepository;
    private final MailStorageService mailStorageService;

    @Autowired
    public MailStorageServiceTest(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        mailStorageService = new MailStorageService(messageRepository);
    }

    @AfterEach
    public void tearDown() {
        messageRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void saveMessageTest() {
        final MessageDto messageDto = MailStorageTestUtils.getMessageDto();
        mailStorageService.addMessage(messageDto);
        final Message message = messageRepository.findByNumber(1L).orElseThrow();
        assertEquals(messageDto.getNumber(), message.getNumber());
        assertEquals(messageDto.getSender(), message.getSender());
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getType(), message.getType());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
    }

    @Test
    public void deleteMessageTest() {
        final MessageDto messageDto = MailStorageTestUtils.getMessageDto();
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        mailStorageService.deleteMessage(messageDto.getNumber());
        final var messageOptional = messageRepository.findByNumber(messageDto.getNumber());
        assertTrue(messageOptional.isEmpty());
    }

    @SneakyThrows
    @Test
    public void updateMessageTest() {
        messageRepository.save(MessageUtils.getMessageFromDto(MailStorageTestUtils.getMessageDto()));
        final var messageDto = MailStorageTestUtils.getMessageDtoForUpdate();
        mailStorageService.updateMessage(messageDto, 1L);
        final var message = messageRepository.findByNumber(1L).orElseThrow();
        assertEquals(messageDto.getSender(), message.getSender());
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
    }

}