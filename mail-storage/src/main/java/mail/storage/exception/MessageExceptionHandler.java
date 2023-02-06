package mail.storage.exception;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Log4j2
public class MessageExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MessageException.class)
    public ResponseEntity<MessageExceptionInfo> handleDraftMessageException(final Exception exception) {
        final MessageExceptionInfo info = createMessageExceptionInfo(exception);
        log.error("Message exception caught");
        log.error(exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(info);
    }

    private MessageExceptionInfo createMessageExceptionInfo(final Exception exception) {
        return MessageExceptionInfo.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
    }
}
