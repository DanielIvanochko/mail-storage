package mail.storage.domain;

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
    private String sender;
    private String receiver;
    private String topic;
    private String body;
    private Date date;
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
