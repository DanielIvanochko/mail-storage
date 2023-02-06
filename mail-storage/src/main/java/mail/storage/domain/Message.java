package mail.storage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
