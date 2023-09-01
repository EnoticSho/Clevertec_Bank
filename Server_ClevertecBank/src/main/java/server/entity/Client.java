package server.entity;

import lombok.*;

@Data
public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
