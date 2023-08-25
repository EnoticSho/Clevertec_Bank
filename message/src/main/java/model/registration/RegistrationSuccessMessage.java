package model.registration;

import lombok.Getter;
import model.Message;
import model.MessageType;

@Getter
public class RegistrationSuccessMessage implements Message {

    private final String message = "Успешная регистрация";
    @Override
    public MessageType getType() {
        return MessageType.REG_OK;
    }
}
