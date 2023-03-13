package mail.storage.exception;


import java.time.LocalDateTime;

public record MessageExceptionInfo(LocalDateTime timeStamp, String message) {
}
