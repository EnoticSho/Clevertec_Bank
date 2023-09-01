package server.servlet;

import lombok.extern.slf4j.Slf4j;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;
import server.service.BankService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class BankServlet extends HttpServlet {

    private final BankService bankService;

    public BankServlet() {
        this.bankService = new BankService(new DatabaseConnectionManager());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int bankId = Integer.parseInt(req.getParameter("bankId"));
            Bank bank = bankService.getBank(bankId);

            if (bank != null) {
                resp.getWriter().write("Bank ID: " + bank.getBankId() + ", Name: " + bank.getName());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Bank not found");
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
            String name = req.getParameter("name");
            Bank bank = new Bank();
            bank.setName(name);
            bankService.createBank(bank);

            resp.getWriter().write("Bank created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int bankId = Integer.parseInt(req.getParameter("bankId"));
            String newName = req.getParameter("name");

            Bank bank = new Bank();
            bank.setBankId(bankId);
            bank.setName(newName);

            bankService.updateBank(bank);

            resp.getWriter().write("Bank updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int bankId = Integer.parseInt(req.getParameter("bankId"));
            bankService.deleteBank(bankId);

            resp.getWriter().write("Bank deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error");
        }
    }
}
