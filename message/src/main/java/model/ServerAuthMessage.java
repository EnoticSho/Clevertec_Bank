package model;

public class ServerAuthMessage implements Message {

    private final String string = "Введите логин и пароль";

    @Override
    public MessageType getType() {
        return MessageType.SERVER_AUTH;
    }

    public String getString() {
        return string;
    }
}
