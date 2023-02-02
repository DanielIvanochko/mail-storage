package mail.storage.controller;

import lombok.RequiredArgsConstructor;
import mail.storage.domain.Message;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberNotFound;
import mail.storage.service.MailStorageService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MailStorageService service;

    //validation is not working... :(
    @PostMapping
    public void addMessage(@Valid @RequestBody final MessageDto messageDto) {
        service.addMessage(messageDto);
    }

    @PutMapping("/{number}")
    public void updateDraftMessage(@RequestBody final UpdateMessageDto updateMessageDto, @PathVariable final Long number) throws DraftMessageException {
        service.updateMessage(updateMessageDto, number);
    }

    @DeleteMapping("/{number}")
    public void deleteMessage(@PathVariable final Long number) {
        service.deleteMessage(number);
    }

    @GetMapping("/{number}")
    public Message findMessageByNumber(@PathVariable final Long number) throws MessageWithNumberNotFound {
        return service.findMessageByNumber(number);
    }

    @GetMapping("/topic")
    public List<Message> findMessagesByTopic(@RequestParam("name") final String topicName) {
        return service.findMessagesByTopic(topicName);
    }

    @GetMapping("/type")
    public List<Message> findMessagesByType(@RequestParam("name") final String typeName) {
        return service.findMessagesByType(typeName);
    }

    @GetMapping("/date")
    public List<Message> findMessagesByDateRange(@RequestBody final DateRangeDto dateRangeDto) {
        return service.findMessagesByDateRange(dateRangeDto);
    }
}
