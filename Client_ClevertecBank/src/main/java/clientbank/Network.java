package clientbank;

import lombok.RequiredArgsConstructor;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@RequiredArgsConstructor
public class Network {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) input.readObject();
    }
}
