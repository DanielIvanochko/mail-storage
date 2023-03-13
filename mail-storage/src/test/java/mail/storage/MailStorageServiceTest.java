package mail.storage;

import lombok.SneakyThrows;
import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;
import mail.storage.repository.MessageRepository;
import mail.storage.service.MailStorageService;
import mail.storage.service.MailStorageServiceImpl;
import mail.storage.util.MessageUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

import static mail.storage.MailStorageTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@PropertySource(value = "application.properties")
class MailStorageServiceTest {
    private final MessageRepository messageRepository;
    private final MailStorageService mailStorageService;

    @Autowired
    MailStorageServiceTest(MessageRepository messageRepository, MailStorageService mailStorageService) {
        this.messageRepository = messageRepository;
        this.mailStorageService = mailStorageService;
    }

    @BeforeEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @Test
    void createMessageThatExists() {
        messageRepository.save(MessageUtils.getMessageFromDto(MailStorageTestUtils.getMessageDto()));
        assertThrows(MessageWithNumberAlreadyExists.class, () -> mailStorageService.addMessage(getMessageDto()));
    }

    @Test
    void updateNotDraftMessage() {
        var messageDto = MailStorageTestUtils.getMessageDto();
        messageDto.setType(MessageType.MAIN);
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        assertThrows(DraftMessageException.class, () -> mailStorageService.updateMessage(getMessageDtoForUpdate(), 1L));
    }

    @Test
    void findNotExistingMessage() {
        assertThrows(MessageWithNumberNotFound.class, () -> mailStorageService.findMessageByNumber(1L));
    }

    @SneakyThrows
    @Test
    void saveMessage() {
        MessageDto messageDto = MailStorageTestUtils.getMessageDto();
        mailStorageService.addMessage(messageDto);
        Message message = messageRepository.findByNumber(1L).orElseThrow();
        assertEquals(messageDto.getNumber(), message.getNumber());
        assertEquals(messageDto.getSender(), message.getSender());
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getType(), message.getType());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
    }

    @Test
    @SneakyThrows
    void deleteMessage() {
        MessageDto messageDto = MailStorageTestUtils.getMessageDto();
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        mailStorageService.deleteMessage(messageDto.getNumber());
        var messageOptional = messageRepository.findByNumber(messageDto.getNumber());
        assertTrue(messageOptional.isEmpty());
    }

    @Test
    void deleteNotExistingMessage() {
        assertThrows(MessageWithNumberNotFound.class, () -> mailStorageService.deleteMessage(1L));
    }

    @SneakyThrows
    @Test
    void updateMessage() {
        messageRepository.deleteAll();
        messageRepository.save(MessageUtils.getMessageFromDto(MailStorageTestUtils.getMessageDto()));
        var messageDto = MailStorageTestUtils.getMessageDtoForUpdate();
        mailStorageService.updateMessage(messageDto, 1L);
        var message = messageRepository.findByNumber(1L).orElseThrow();
        assertEquals(messageDto.getSender(), message.getSender());
        assertEquals(messageDto.getBody(), message.getBody());
        assertEquals(messageDto.getReceiver(), message.getReceiver());
        assertEquals(messageDto.getAttachmentUrl(), message.getAttachmentUrl());
    }

    @Test
    void getMessagesByTopic() {
        addTestMessagesToDb();
        var messagesByTopic = mailStorageService.findMessagesByTopic("emergency");
        var expectedMessagesByTopic = getMessagesByCriteria(message -> message.getTopic().equals("emergency"));
        assertEqualityOfTwoMessageList(expectedMessagesByTopic, messagesByTopic);
    }

    @Test
    void getMessagesByType() {
        addTestMessagesToDb();
        var messagesByType = mailStorageService.findMessagesByType("ad");
        var expectedMessagesByType = getMessagesByCriteria(message -> message.getType().equals(MessageType.AD));
        assertEqualityOfTwoMessageList(expectedMessagesByType, messagesByType);
    }

    @Test
    void getMessagesByDateRange() {
        addTestMessagesToDb();
        final var beginDate = MailStorageTestUtils.getBeginDate();
        final var endDate = MailStorageTestUtils.getEndDate();
        final DateRangeDto dateRangeDto = DateRangeDto.builder()
                .beginDate(beginDate)
                .endDate(endDate)
                .build();
        var messagesByDateRange = mailStorageService.findMessagesByDateRange(dateRangeDto);
        var expectedMessages = getMessagesByCriteria(message -> isMessageDateInRange(message, dateRangeDto));
        assertEqualityOfTwoMessageList(expectedMessages, messagesByDateRange);
    }

    @Test
    void constructorTest() {
        MailStorageServiceImpl service = new MailStorageServiceImpl(messageRepository);
        assertEquals(messageRepository, service.getRepository());
    }

    void assertEqualityOfTwoMessageList(final List<Message> expected, final List<Message> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    void addTestMessagesToDb() {
        final var messagesList = MailStorageTestUtils.getTestMessages();
        messageRepository.saveAll(messagesList);
    }

}