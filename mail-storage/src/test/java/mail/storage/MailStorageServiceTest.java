package mail.storage;

import lombok.SneakyThrows;
import mail.storage.domain.Message;
import mail.storage.domain.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.repository.MessageRepository;
import mail.storage.service.MailStorageService;
import mail.storage.util.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

import static mail.storage.MailStorageTestUtils.getMessagesByCriteria;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@PropertySource(value = "application.properties")
class MailStorageServiceTest {
    private final MessageRepository messageRepository;
    private final MailStorageService mailStorageService;

    @Autowired
    MailStorageServiceTest(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        mailStorageService = new MailStorageService(messageRepository);
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void saveMessageTest() {
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
    void deleteMessageTest() {
        final MessageDto messageDto = MailStorageTestUtils.getMessageDto();
        messageRepository.save(MessageUtils.getMessageFromDto(messageDto));
        mailStorageService.deleteMessage(messageDto.getNumber());
        final var messageOptional = messageRepository.findByNumber(messageDto.getNumber());
        assertTrue(messageOptional.isEmpty());
    }

    @SneakyThrows
    @Test
    void updateMessageTest() {
        messageRepository.save(MessageUtils.getMessageFromDto(MailStorageTestUtils.getMessageDto()));
        final var messageDto = MailStorageTestUtils.getMessageDtoForUpdate();
        mailStorageService.updateMessage(messageDto, 1L);
        final var message = messageRepository.findByNumber(1L).orElseThrow();
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

    boolean isMessageDateInRange(final Message message, final DateRangeDto dateRangeDto) {
        final var messageDate = message.getDate();
        return (messageDate.after(dateRangeDto.getBeginDate())
                || messageDate.equals(dateRangeDto.getBeginDate()))
                &&
                (message.getDate().before(dateRangeDto.getEndDate())
                        || messageDate.equals(dateRangeDto.getEndDate()));
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