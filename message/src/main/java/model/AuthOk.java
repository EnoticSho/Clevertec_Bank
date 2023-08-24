package model;

import lombok.Getter;

@Getter
public class AuthOk implements Message {

    private final String authOk = "Успешная авторизация";

    @Override
    public MessageType getType() {
        return MessageType.AUTH_OK;
    }
}
