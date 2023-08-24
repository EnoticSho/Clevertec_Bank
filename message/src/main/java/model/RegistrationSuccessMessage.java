package model;

import lombok.Getter;

@Getter
public class RegistrationSuccessMessage implements Message {

    private final String message = "Успешная регистрация";
    @Override
    public MessageType getType() {
        return MessageType.REG_OK;
    }
}
