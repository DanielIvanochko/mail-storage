package mail.storage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMessageDto {
    @NotEmpty(message = "Sender's email must not be empty")
    @Email(message = "sender's email is not correct")
    private String sender;
    @NotEmpty(message = "Receiver's email must not be empty")
    @Email(message = "Receiver's email is not correct")
    private String receiver;
    private String topic;
    private String body;
    private String attachmentUrl;
    private MessageType type;
}
