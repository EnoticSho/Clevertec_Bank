package model.auth;

import model.Message;
import model.MessageType;

public class AuthResponse implements Message {

    private final String authOk = "Успешная авторизация";

    @Override
    public MessageType getType() {
        return MessageType.AUTH_OK;
    }

    public String getAuthOk() {
        return authOk;
    }
}
