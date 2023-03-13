package mail.storage.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document("message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true)
    private Long number;
    @Email
    private String sender;
    @Email
    private String receiver;
    private String topic;
    private String body;
    @NotEmpty
    private Date date;
    @Pattern(regexp = "^(http|https)://[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(/{0,1}[a-zA-Z0-9\\-\\.\\?\\,\\'/\\\\\\+&amp;%\\$#_]*)?$")
    private String attachmentUrl;

    private MessageType type;

    @RequiredArgsConstructor
    public enum MessageType {
        MAIN(0),
        SPAM(1),
        AD(2),
        DRAFT(3),
        FAVOURITE(4),
        TRASH(5);
        final int value;
    }
}
