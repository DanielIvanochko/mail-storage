package mail.storage.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;
import mail.storage.dto.*;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;
import mail.storage.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
@Getter
public class MailStorageServiceImpl implements MailStorageService {
    private final MessageRepository repository;

    public void addMessage(MessageDto messageDto) throws MessageWithNumberAlreadyExists {
        repository.findByNumber(messageDto.getNumber());
        if (numberOfMessageAlreadyExists(messageDto)) {
            throw new MessageWithNumberAlreadyExists(String.format("Message with number %d already exists", messageDto.getNumber()));
        } else {
            repository.save(new Message(messageDto));
        }
    }

    private boolean numberOfMessageAlreadyExists(MessageDto messageDto) {
        return repository.existsByNumber(messageDto.getNumber());
    }

    public Message updateMessage(UpdateMessageDto updateMessageDto, Long number) throws DraftMessageException, MessageWithNumberNotFound {
        final Message message = findMessageByNumber(number);
        if (message.isDraft()) {
            message.updateWithDto(updateMessageDto);
            return repository.save(message);
        } else {
            throw new DraftMessageException("The message must be draft for update");
        }
    }

    public Message findMessageByNumber(Long number) throws MessageWithNumberNotFound {
        return repository.findByNumber(number)
                .orElseThrow(() -> new MessageWithNumberNotFound(String.format("The message with number %d was not found", number)));
    }

    public List<Message> findMessagesByType(MsgType type) {
        return repository.findByType(MessageType.valueOf(type.getTypeName().toUpperCase()));
    }

    public List<Message> findMessagesByTopic(MsgTopic topic) {
        return repository.findByTopic(topic.getTopicName());
    }

    public void deleteMessage(Long number) throws MessageWithNumberNotFound {
        Message message = findMessageByNumber(number);
        repository.delete(message);
    }

    public List<Message> findMessagesByDateRange(DateRangeDto dateRangeDto) {
        return repository.findByDateRange(dateRangeDto.getBeginDate(), dateRangeDto.getEndDate());
    }
}
