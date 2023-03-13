package mail.storage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail.storage.domain.Message.MessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @NotEmpty(message = "Sender's email must not be empty")
    @Email(message = "sender's email is not correct")
    private String sender;
    @NotEmpty(message = "Receiver's email must not be empty")
    @Email(message = "Receiver's email is not correct")
    private String receiver;
    private Long number;
    private String topic;
    private String body;
    @Pattern(regexp = "^(http|https)://[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(/{0,1}[a-zA-Z0-9\\-\\.\\?\\,\\'/\\\\\\+&amp;%\\$#_]*)?$")
    private String attachmentUrl;
    private MessageType type;
}
