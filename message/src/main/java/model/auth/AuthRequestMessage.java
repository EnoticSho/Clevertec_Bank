package model.auth;

import model.Message;
import model.MessageType;

public class AuthRequestMessage implements Message {

    private final String login;
    private final String password;

    public AuthRequestMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public MessageType getType() {
        return MessageType.AUTH;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
