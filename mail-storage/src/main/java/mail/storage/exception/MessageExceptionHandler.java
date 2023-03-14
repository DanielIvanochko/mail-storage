package mail.storage.exception;

import lombok.extern.log4j.Log4j2;
import mail.storage.exception.DraftMessageException;
import mail.storage.exception.MessageExceptionInfo;
import mail.storage.exception.MessageWithNumberAlreadyExists;
import mail.storage.exception.MessageWithNumberNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class MessageExceptionHandler {
    @ExceptionHandler(value = {DraftMessageException.class, MessageWithNumberAlreadyExists.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageExceptionInfo handleBadRequestMessageException(Exception exception) {
        MessageExceptionInfo info = createMessageExceptionInfo(exception);
        log.error("Message exception caught: " + exception.getMessage());
        return info;
    }

    @ExceptionHandler(value = MessageWithNumberNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageExceptionInfo handleNotFoundMessageException(Exception exception) {
        MessageExceptionInfo info = createMessageExceptionInfo(exception);
        log.error("Message exception caught: " + exception.getMessage());
        return info;
    }

    private MessageExceptionInfo createMessageExceptionInfo(Exception exception) {
        return new MessageExceptionInfo(LocalDateTime.now(), exception.getMessage());
    }
}
