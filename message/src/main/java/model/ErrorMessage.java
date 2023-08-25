package model;

public class ErrorMessage implements Message {

    private final String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR_MESSAGE;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
