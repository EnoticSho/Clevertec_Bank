package server.service;

import server.dao.ClientDAO;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

//    public boolean isClientExists(String login, String password) {
//        return clientDAO.isClientExists(login, password);
//    }


}
