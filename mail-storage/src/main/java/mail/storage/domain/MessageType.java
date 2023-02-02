package mail.storage.domain;

public enum MessageType {
    MAIN(0),
    SPAM(1),
    AD(2),
    DRAFT(3),
    FAVOURITE(4),
    TRASH(5);
    final int value;
    MessageType(final int value){
        this.value = value;
    }
}
