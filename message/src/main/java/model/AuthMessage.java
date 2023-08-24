package model;

public class AuthMessage implements Message {

    private final String login;
    private final String password;

    public AuthMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public MessageType getType() {
        return MessageType.AUTH;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
