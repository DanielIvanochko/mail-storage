package mail.storage.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMessageDto {
    private String sender;
    private String receiver;
    private String topic;
    private String body;
    private String attachmentUrl;
}
