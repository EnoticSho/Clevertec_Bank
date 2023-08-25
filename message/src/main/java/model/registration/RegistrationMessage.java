package model.registration;

import lombok.Getter;
import model.Message;
import model.MessageType;

@Getter
public class RegistrationMessage implements Message {

    private final String login;

    private final String password;

    public RegistrationMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public MessageType getType() {
        return MessageType.REGISTRATION;
    }
}
