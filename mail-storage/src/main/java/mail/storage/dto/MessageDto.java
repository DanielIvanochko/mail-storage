package mail.storage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail.storage.domain.MessageType;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @NotEmpty(message = "sender should not be empty")
    @Email(message = "sender's email is not correct")
    private String sender;
    @NotEmpty(message = "receiver should not be empty")
    @Email(message = "receiver's email is not correct")
    private String receiver;
    private Long number;
    private String topic;
    private String body;
    private String attachmentUrl;
    private MessageType type;
}
