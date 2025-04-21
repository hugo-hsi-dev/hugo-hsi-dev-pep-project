package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username.length() == 0) {
            return null;
        }

        if (password.length() < 4) {
            return null;
        }

        Boolean accountExists = accountDAO.checkExistingUsername(account.getUsername());
        if (accountExists) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    public Account login(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account);
    }
}
