package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

@Getter
public class StatementResponseMessage implements Message {
    private final String message;

    public StatementResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public MessageType getType() {
        return null;
    }
}
