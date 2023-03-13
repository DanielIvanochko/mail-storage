package mail.storage.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestControllerAdvice
@Log4j2
public class MessageExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MessageException.class)
    public ResponseEntity<MessageExceptionInfo> handleDraftMessageException(Exception exception) {
        MessageExceptionInfo info = createMessageExceptionInfo(exception);
        log.error("Message exception caught:\n" + exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(info);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(handleArgumentException(ex));
    }

    private Map<String, String> handleArgumentException(MethodArgumentNotValidException exception) {
        return exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
    }

    private MessageExceptionInfo createMessageExceptionInfo(Exception exception) {
        return new MessageExceptionInfo(LocalDateTime.now(), exception.getMessage());
    }
}
