package model;

public class ExitMessage implements Message{
    @Override
    public MessageType getType() {
        return MessageType.Exit;
    }
}
