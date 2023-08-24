package clientbank;

import model.AuthMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        new BankClient();
    }
}
