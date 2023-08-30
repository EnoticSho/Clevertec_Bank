package clientbank;

public class ClientRunner {
    public static void main(String[] args) {
        BankService bankService = new BankService();
        BankClient bankClient = new BankClient(bankService);
        bankClient.start();
    }
}
