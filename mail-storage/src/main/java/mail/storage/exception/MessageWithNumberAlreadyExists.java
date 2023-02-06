package mail.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class MessageWithNumberAlreadyExists extends MessageException {
    public MessageWithNumberAlreadyExists(String message) {
        super(message);
    }
}
