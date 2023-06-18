package mail.storage.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mail.storage.domain.Message;
import mail.storage.dto.*;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;
import mail.storage.service.MailStorageService;
import org.springframework.cache.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MailStorageService service;

    @PostMapping
    public void addMessage(@Valid @RequestBody MessageDto messageDto) throws MessageWithNumberAlreadyExists {
        service.addMessage(messageDto);
    }

    @PutMapping("/{number}")
    @Caching(
            put = {
                    @CachePut(key = "#number", value = "message")
            },
            evict = {
                    @CacheEvict(cacheNames = {"topic", "type"}, allEntries = true)
            }
    )
    public Message updateDraftMessage(@Valid @RequestBody UpdateMessageDto updateMessageDto, @PathVariable Long number)
            throws DraftMessageException, MessageWithNumberNotFound {
        return service.updateMessage(updateMessageDto, number);
    }

    @DeleteMapping("/{number}")
    @Caching(
            evict = {
                    @CacheEvict(key = "#number", value = "message"),
                    @CacheEvict(cacheNames = {"topic", "type"}, allEntries = true)
            }
    )
    public void deleteMessage(@PathVariable Long number) throws MessageWithNumberNotFound {
        service.deleteMessage(number);
    }

    @GetMapping("/{number}")
    @Cacheable(key = "#number", value = "message")
    public Message findMessageByNumber(@PathVariable Long number) throws MessageWithNumberNotFound {
        return service.findMessageByNumber(number);
    }

    @GetMapping("/topics/{topicName}")
    @Cacheable(key = "#topicName", value = "topic")
    public List<Message> findMessagesByTopic(@PathVariable String topicName) {
        return service.findMessagesByTopic(new MsgTopic(topicName));
    }

    @GetMapping("/types/{typeName}")
    @Cacheable(key = "#typeName", value = "type")
    public List<Message> findMessagesByType(@PathVariable String typeName) {
        return service.findMessagesByType(new MsgType(typeName));
    }

    @GetMapping("/date")
    public List<Message> findMessagesByDateRange(@RequestBody DateRangeDto dateRangeDto) {
        return service.findMessagesByDateRange(dateRangeDto);
    }
}
