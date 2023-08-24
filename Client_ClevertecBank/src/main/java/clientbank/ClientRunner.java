package clientbank;

import model.AuthMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        BankClient bankClient = new BankClient();

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while ((userInput = stdin.readLine()) != null) {
            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }
            bankClient.sendMessage(new AuthMessage("1","2"));
        }
    }
}
