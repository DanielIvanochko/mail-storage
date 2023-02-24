package mail.storage.service;

import mail.storage.domain.Message;
import mail.storage.domain.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;

import java.util.List;

public interface MailStorageService {
    void addMessage(MessageDto messageDto) throws MessageWithNumberAlreadyExists;

    void deleteMessage(Long number);

    Message updateMessage(UpdateMessageDto updateMessageDto, Long number) throws DraftMessageException, MessageWithNumberNotFound;

    Message findMessageByNumber(Long number) throws MessageWithNumberNotFound;

    List<Message> findMessagesByType(String type);

    List<Message> findMessagesByTopic(String topic);

    List<Message> findMessagesByDateRange(DateRangeDto dateRangeDto);
}
