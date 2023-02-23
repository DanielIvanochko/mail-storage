package mail.storage.util;

import mail.storage.domain.Message;
import mail.storage.dto.MessageDto;
import mail.storage.dto.UpdateMessageDto;

import java.util.Date;

public class MessageUtils {
    private MessageUtils() {
    }

    public static Message getMessageFromDto(MessageDto messageDto) {
        return Message.builder()
                .body(messageDto.getBody())
                .topic(messageDto.getTopic())
                .type(messageDto.getType())
                .date(new Date())
                .attachmentUrl(messageDto.getAttachmentUrl())
                .sender(messageDto.getSender())
                .receiver(messageDto.getReceiver())
                .number(messageDto.getNumber())
                .build();
    }

    public static void updateMessageWithDto(Message message, UpdateMessageDto messageDto) {
        message.setBody(messageDto.getBody());
        message.setSender(messageDto.getSender());
        message.setReceiver(messageDto.getReceiver());
        message.setTopic(messageDto.getTopic());
        message.setAttachmentUrl(messageDto.getAttachmentUrl());
    }
}
