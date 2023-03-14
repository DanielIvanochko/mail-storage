package mail.storage;

import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class MailStorageTestUtils {
    private static final LocalDateTime beginDate = LocalDateTime.now();
    private static final LocalDateTime endDate = LocalDateTime.now().plusDays(5);
    private static final List<Message> messages = List.of(new Message("some_id1",
                    1L,
                    "sender1@gmail.com",
                    "receiver1@gmail.com",
                    "meeting",
                    "hello, how is your life going?", beginDate,
                    "attachment.url", MessageType.MAIN),
            new Message("some_id2",
                    2L,
                    "sender2@gmail.com",
                    "receiver2@gmail.com",
                    "emergency",
                    "hello, how is your life going?", beginDate,
                    "attachment.url", MessageType.DRAFT),
            new Message("some_id3",
                    3L,
                    "sender3@gmail.com",
                    "receiver3@gmail.com",
                    "emergency",
                    "hello, how is your life going?", beginDate,
                    "attachment.url", MessageType.DRAFT),
            new Message("some_id4",
                    4L,
                    "sender4@gmail.com",
                    "receiver4@gmail.com",
                    "health",
                    "hello, how is your life going?", beginDate,
                    "attachment.url", MessageType.AD),
            new Message("some_id5",
                    5L,
                    "sender5@gmail.com",
                    "receiver5@gmail.com",
                    "health",
                    "hello, how is your life going?", beginDate,
                    "attachment.url", MessageType.AD),
            new Message("some_id6",
                    6L,
                    "sender6@gmail.com",
                    "receiver6@gmail.com",
                    "support",
                    "hello, how is your life going?", beginDate,
                    "attachment.url", MessageType.FAVOURITE));

    public static UpdateMessageDto getMessageDtoForUpdate() {
        return UpdateMessageDto.builder()
                .sender("sigma.software2@gmail.com")
                .receiver("me@facebook.com")
                .attachmentUrl("hello.to.me.com")
                .body("Hello wassup")
                .build();
    }

    public static LocalDateTime getBeginDate() {
        return beginDate;
    }

    public static LocalDateTime getEndDate() {
        return endDate;
    }

    public static MessageDto getMessageDto() {
        return MessageDto.builder()
                .number(1L)
                .sender("daniel.ivanochko@gmail.com")
                .receiver("sigma.software@gmail.com")
                .topic("meeting")
                .type(MessageType.DRAFT)
                .body("Please connect to the meeting")
                .attachmentUrl("https://www.google.com/images/1")
                .build();
    }

    public static List<Message> getMessagesByCriteria(final Predicate<Message> messagePredicate) {
        return messages.stream()
                .filter(messagePredicate)
                .toList();
    }

    public static List<Message> getTestMessages() {
        return messages;
    }

    public static boolean isMessageDateInRange(final Message message, final DateRangeDto dateRangeDto) {
        final var messageDate = message.getDate();
        return (messageDate.isAfter(dateRangeDto.getBeginDate())
                || messageDate.equals(dateRangeDto.getBeginDate()))
                &&
                (message.getDate().isBefore(dateRangeDto.getEndDate())
                        || messageDate.equals(dateRangeDto.getEndDate()));
    }
}
