package mail.storage.service;

import lombok.RequiredArgsConstructor;
import mail.storage.domain.Message;
import mail.storage.domain.MessageType;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.repository.MessageRepository;
import mail.storage.util.MessageUtils;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailStorageService {
    private final MessageRepository repository;

    public void addMessage(final MessageDto messageDto) {
        repository.save(MessageUtils.getMessageFromDto(messageDto));
    }

    public void updateMessage(final UpdateMessageDto updateMessageDto, final Long number) throws DraftMessageException {
        final Message message = repository.findByNumber(number).orElseThrow();
        if(!isMessageDraft(message)){
            throw new DraftMessageException("The message must be draft for update");
        }
        MessageUtils.updateMessageWithDto(message, updateMessageDto);
        repository.save(message);
    }

    private boolean isMessageDraft(final Message message) {
        return message.getType().equals(MessageType.DRAFT);
    }


    public void deleteMessage(Long number) {
        repository.deleteByNumber(number);
    }
}
