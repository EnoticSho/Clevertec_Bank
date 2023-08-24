package clientbank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.Socket;

@Getter
@Setter
@AllArgsConstructor
public class Network<I, O> {
    private Socket socket;
    private final I inputStream;
    private final O outputStream;

}
