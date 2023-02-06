package mail.storage.controller;

import lombok.RequiredArgsConstructor;
import mail.storage.domain.Message;
import mail.storage.dto.DateRangeDto;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;
import mail.storage.service.MailStorageService;
import org.springframework.cache.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MailStorageService service;

    @GetMapping("/test1")
    public String test() {
        return "wassup ma men";
    }

    //validation is not working... :(
    @PostMapping
    public void addMessage(@Valid @RequestBody final MessageDto messageDto) throws MessageWithNumberAlreadyExists {
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
    public Message updateDraftMessage(@RequestBody final UpdateMessageDto updateMessageDto, @PathVariable final Long number) throws DraftMessageException {
        return service.updateMessage(updateMessageDto, number);
    }

    @DeleteMapping("/{number}")
    @Caching(
            evict = {
                    @CacheEvict(key = "#number", value = "message"),
                    @CacheEvict(cacheNames = {"topic", "type"}, allEntries = true)
            }
    )
    public void deleteMessage(@PathVariable final Long number) {
        service.deleteMessage(number);
    }

    @GetMapping("/{number}")
    @Cacheable(key = "#number", value = "message")
    public Message findMessageByNumber(@PathVariable final Long number) throws MessageWithNumberNotFound {
        return service.findMessageByNumber(number);
    }

    @GetMapping("/topic")
    @Cacheable(key = "#topicName", value = "topic")
    public List<Message> findMessagesByTopic(@RequestParam("name") final String topicName) {
        return service.findMessagesByTopic(topicName);
    }

    @GetMapping("/type")
    @Cacheable(key = "#typeName", value = "type")
    public List<Message> findMessagesByType(@RequestParam("name") final String typeName) {
        return service.findMessagesByType(typeName);
    }

    @GetMapping("/date")
    public List<Message> findMessagesByDateRange(@RequestBody final DateRangeDto dateRangeDto) {
        return service.findMessagesByDateRange(dateRangeDto);
    }
}
