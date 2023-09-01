package server.servlet;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Client;
import server.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ClientServlet extends HttpServlet {

    private final ClientService clientService = new ClientService(new DatabaseConnectionManager());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int clientId = Integer.parseInt(req.getParameter("clientId"));
            Client client = clientService.getClient(clientId);

            if (client != null) {
                resp.getWriter().write("Client ID: " + client.getClientId() + ", Name: " + client.getFirstName() + " " + client.getLastName());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Client not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            Client client = new Client();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setUsername(username);
            client.setPassword(password);
            clientService.createClient(client);

            resp.getWriter().write("Client created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int clientId = Integer.parseInt(req.getParameter("clientId"));
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            Client client = new Client();
            client.setClientId(clientId);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setUsername(username);
            client.setPassword(password);

            clientService.updateClient(client);

            resp.getWriter().write("Client updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int clientId = Integer.parseInt(req.getParameter("clientId"));
            clientService.deleteClient(clientId);

            resp.getWriter().write("Client deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }
}
