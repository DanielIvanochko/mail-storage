package mail.storage.controller;

import lombok.RequiredArgsConstructor;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import mail.storage.exception.DraftMessageException;
import mail.storage.service.MailStorageService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    /*
    * 1. get message by number
    * 2. get messages by date range
    * 3. get messages by topic
    * 4. get favourite messages
    * 5. get messages by type
    * */
}
