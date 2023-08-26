package clientbank;

public class ClientRunner {
    public static void main(String[] args) {
        BankClient bankClient = new BankClient(new BankService());
        bankClient.start();
    }
}
