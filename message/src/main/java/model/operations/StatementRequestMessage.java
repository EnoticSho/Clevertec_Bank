package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.time.LocalDate;

@Getter
public class StatementRequestMessage implements Message {
    private final LocalDate start;
    private final LocalDate end;

    public StatementRequestMessage(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public MessageType getType() {
        return MessageType.Deposit;
    }
}
