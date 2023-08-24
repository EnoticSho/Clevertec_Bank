package server;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                BankServer.broadcast(message); // Broadcast the message to all clients
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                BankServer.clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send a message to this client
    public void sendMessage(String message) {
        out.println(message);
    }
}
