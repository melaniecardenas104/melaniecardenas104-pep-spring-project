package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account insertUser(Account account) {
        return accountDAO.insertUser(account);
    }

    public Account checkLogin(Account account) {
        return accountDAO.checkLogin(account);
    }
    
}
