package model.auth;

import lombok.Getter;
import model.Message;
import model.MessageType;

@Getter
public class AuthResponse implements Message {

    private final boolean authOk;

    public AuthResponse(boolean authOk) {
        this.authOk = authOk;
    }

    public boolean getAuthOk() {
        return authOk;
    }

    @Override
    public MessageType getType() {
        return MessageType.AUTH_OK;
    }
}
