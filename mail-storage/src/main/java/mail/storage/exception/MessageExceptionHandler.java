package mail.storage.exception;

import lombok.extern.log4j.Log4j2;
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
        return processException(exception);
    }

    @ExceptionHandler(value = MessageWithNumberNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageExceptionInfo handleNotFoundMessageException(Exception exception) {
        return processException(exception);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageExceptionInfo handleUnknownException(Exception exception) {
        return processException(exception);
    }

    private MessageExceptionInfo processException(Exception exception) {
        MessageExceptionInfo info = createMessageExceptionInfo(exception);
        log.error("Message exception caught: " + exception.getMessage());
        return info;
    }

    private MessageExceptionInfo createMessageExceptionInfo(Exception exception) {
        return new MessageExceptionInfo(LocalDateTime.now(), exception.getMessage());
    }
}
