package model;

public class AuthOk implements Message {

    private final String authOk = "Успешная авторизация";

    @Override
    public MessageType getType() {
        return MessageType.AUTH_OK;
    }

    public String getAuthOk() {
        return authOk;
    }
}
