package clientbank;

import java.io.*;
import java.net.*;

public class BankClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8761;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server. Type 'exit' to quit.");

            String userInput;
            while ((userInput = stdin.readLine()) != null) {
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
                out.println(userInput);
                String serverResponse = in.readLine();
                System.out.println("Server says: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
