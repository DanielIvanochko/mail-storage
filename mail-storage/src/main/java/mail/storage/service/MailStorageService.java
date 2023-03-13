package mail.storage.service;

import mail.storage.domain.Message;
import mail.storage.dto.*;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;

import java.util.List;

public interface MailStorageService {
    void addMessage(MessageDto messageDto) throws MessageWithNumberAlreadyExists;

    void deleteMessage(Long number) throws MessageWithNumberNotFound;

    Message updateMessage(UpdateMessageDto updateMessageDto, Long number) throws DraftMessageException, MessageWithNumberNotFound;

    Message findMessageByNumber(Long number) throws MessageWithNumberNotFound;

    List<Message> findMessagesByType(MsgType type);

    List<Message> findMessagesByTopic(MsgTopic topic);

    List<Message> findMessagesByDateRange(DateRangeDto dateRangeDto);
}
