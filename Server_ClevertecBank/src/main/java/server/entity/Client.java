package server.entity;

import lombok.*;

@Data
@Builder
public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
