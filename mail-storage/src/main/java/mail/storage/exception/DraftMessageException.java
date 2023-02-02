package mail.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DraftMessageException extends Exception {
    public DraftMessageException(final String message) {
        super(message);
    }
}
