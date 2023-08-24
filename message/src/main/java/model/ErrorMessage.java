package model;

import lombok.Getter;

@Getter
public class ErrorMessage implements Message {

    private final String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR_MESSAGE;
    }
}
