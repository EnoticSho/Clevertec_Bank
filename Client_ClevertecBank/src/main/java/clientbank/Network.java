package clientbank;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@RequiredArgsConstructor
@AllArgsConstructor
public class Network {

    private ObjectInputStream input;
    private ObjectOutputStream output;

    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) input.readObject();
    }

    public void close() throws IOException {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }
}
