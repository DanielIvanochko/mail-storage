package mail.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MessageWithNumberNotFound extends Exception {
    public MessageWithNumberNotFound(String message) {
        super(message);
    }
}
