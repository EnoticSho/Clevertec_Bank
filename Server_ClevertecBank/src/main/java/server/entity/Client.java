package server.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
