package model.operations;

import model.Message;
import model.MessageType;

public class BalanceRequestMessage implements Message {

    @Override
    public MessageType getType() {
        return MessageType.Balance;
    }
}
