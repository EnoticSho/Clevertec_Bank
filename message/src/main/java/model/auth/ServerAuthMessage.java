package model.auth;

import lombok.Getter;
import model.Message;
import model.MessageType;

@Getter
public class ServerAuthMessage implements Message {

    private final String string = "Введите логин и пароль";

    @Override
    public MessageType getType() {
        return MessageType.SERVER_AUTH;
    }

}
