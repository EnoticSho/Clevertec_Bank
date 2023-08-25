package model.operations;

import model.Message;
import model.MessageType;

public class ServerOperationMessage implements Message {
    private final String operations = """
            1. Проверить остаток
            2. Снять
            3. Пополнить
            4. Перевести
            5. Выйти
            """;

    @Override
    public MessageType getType() {
        return MessageType.SERVER_AUTH;
    }

    public String getOperations() {
        return operations;
    }
}
