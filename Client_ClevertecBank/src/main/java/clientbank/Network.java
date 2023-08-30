package clientbank;

import lombok.RequiredArgsConstructor;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Represents the network communication module to handle sending and receiving messages.
 * It uses Object streams to serialize and deserialize messages.
 */
@RequiredArgsConstructor
public class Network {

    /** Input stream to receive messages. */
    private final ObjectInputStream input;

    /** Output stream to send messages. */
    private final ObjectOutputStream output;

    /**
     * Sends a message over the network.
     *
     * @param message The message to be sent.
     * @throws IOException If there's an error while writing the object to the output stream.
     */
    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    /**
     * Receives a message from the network.
     *
     * @return The received message.
     * @throws IOException            If there's an error while reading from the input stream.
     * @throws ClassNotFoundException If the received object is not of type Message.
     */
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) input.readObject();
    }
}
