package mail.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail.storage.domain.MessageType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @NotEmpty(message = "sender should not be empty")
    @Email
    private String sender;
    @NotEmpty(message = "receiver should not be empty")
    @Email
    private String receiver;
    private Long number;
    private String topic;
    private String body;
    private String attachmentUrl;
    private MessageType type;
}
