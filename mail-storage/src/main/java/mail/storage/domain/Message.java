package mail.storage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document("message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"date"})
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
    private LocalDateTime date;
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

    public Message(MessageDto messageDto) {
        body = messageDto.getBody();
        topic = messageDto.getTopic();
        type = messageDto.getType();
        date = LocalDateTime.now();
        attachmentUrl = messageDto.getAttachmentUrl();
        sender = messageDto.getSender();
        receiver = messageDto.getReceiver();
        number = messageDto.getNumber();
    }

    @JsonIgnore
    public boolean isDraft() {
        return type != null && type.equals(MessageType.DRAFT);
    }

    public void updateWithDto(UpdateMessageDto messageDto) {
        body = messageDto.getBody();
        topic = messageDto.getTopic();
        type = messageDto.getType();
        attachmentUrl = messageDto.getAttachmentUrl();
        sender = messageDto.getSender();
        receiver = messageDto.getReceiver();
    }

}
