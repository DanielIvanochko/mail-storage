package mail.storage.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mail.storage.domain.Message;
import mail.storage.domain.MessageType;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;
import mail.storage.repository.MessageRepository;
import mail.storage.util.MessageUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
@Getter
public class MailStorageService {
    private final MessageRepository repository;

    public void addMessage(final MessageDto messageDto) throws MessageWithNumberAlreadyExists {
        try {
            repository.save(MessageUtils.getMessageFromDto(messageDto));
        } catch (final DataIntegrityViolationException exception) {
            throw new MessageWithNumberAlreadyExists("Message with such number already exists");
        }
    }

    public Message updateMessage(final UpdateMessageDto updateMessageDto, final Long number) throws DraftMessageException {
        final Message message = repository.findByNumber(number).orElseThrow();
        if (!isMessageDraft(message)) {
            throw new DraftMessageException("The message must be draft for update");
        }
        MessageUtils.updateMessageWithDto(message, updateMessageDto);
        return repository.save(message);
    }

    public Message findMessageByNumber(final Long number) throws MessageWithNumberNotFound {
        return repository.findByNumber(number).orElseThrow(() -> new MessageWithNumberNotFound(String.format("The message with number %d was not found", number)));
    }

    public List<Message> findMessagesByType(final String type) {
        return repository.findByType(MessageType.valueOf(type.toUpperCase()));
    }

    public List<Message> findMessagesByTopic(final String topic) {
        return repository.findByTopic(topic);
    }

    private boolean isMessageDraft(final Message message) {
        return message.getType().equals(MessageType.DRAFT);
    }


    public void deleteMessage(final Long number) {
        repository.deleteByNumber(number);
    }

    public List<Message> findMessagesByDateRange(DateRangeDto dateRangeDto) {
        return repository.findByDateRange(dateRangeDto.getBeginDate(), dateRangeDto.getEndDate());
    }
}
