package model;

import java.math.BigDecimal;

public class BalanceMessage implements Message{

    private BigDecimal balance;

    public BalanceMessage(BigDecimal balance) {
        this.balance = balance;
    }

    public BalanceMessage() {}

    @Override
    public MessageType getType() {
        return null;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
