package mail.storage.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MessageExceptionInfo {
    private final LocalDateTime timeStamp;
    private final String message;
}
