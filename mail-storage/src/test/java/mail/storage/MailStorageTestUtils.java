package mail.storage;

import mail.storage.domain.MessageType;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;

public class MailStorageTestUtils {

    public static UpdateMessageDto getMessageDtoForUpdate(){
        return UpdateMessageDto.builder()
                .sender("sigma.software2@gmail.com")
                .receiver("me@facebook.com")
                .attachmentUrl("hello.to.me.com")
                .body("Hello wassup")
                .build();
    }

    public static MessageDto getMessageDto() {
        return MessageDto.builder()
                .number(1L)
                .sender("daniel.ivanochko@gmail.com")
                .receiver("sigma.software@gmail.com")
                .topic("meeting")
                .type(MessageType.DRAFT)
                .body("Please connect to the meeting")
                .attachmentUrl("www.google.com/images/1")
                .build();
    }
}
