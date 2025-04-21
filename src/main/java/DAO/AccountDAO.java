package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // initialize connections
    Connection connection = ConnectionUtil.getConnection();

    // Get account by username and password
    public Account getAccountByUsernameAndPassword(Account account) {

        try {
            // SQL query;
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // inject parameters
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Check for existing account
    public Boolean checkExistingUsername(String username) {
        try {
       // SQL query;
       String sql = "SELECT COUNT(*) FROM account WHERE username = ?;";
       PreparedStatement preparedStatement = connection.prepareStatement(sql);

       preparedStatement.setString(1, username);

       ResultSet rs = preparedStatement.executeQuery();

       if (rs.next()) {
        int rowCount = rs.getInt(1);
        return rowCount > 0;
       }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean checkExistingAccountId(int id) {
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

       preparedStatement.setInt(1, id);

       ResultSet rs = preparedStatement.executeQuery();

       if (rs.next()) {
        int rowCount = rs.getInt(1);
        return rowCount > 0;
       }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Insert new account
    public Account insertAccount(Account account) {
        try {
            // SQL 
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // inject parameters
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
