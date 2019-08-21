import spark.Request;
import spark.Response;

import java.util.HashMap;

public class Accounts {
    private HashMap<String, Account> accounts;

    public Accounts() {
        accounts = new HashMap<>();
    }

    public boolean addAccount(Account account) {
        String guid = account.getGuid().toString();
        if (accounts.containsKey(guid))
            return false;

        accounts.put(guid, account);
        return true;
    }

    public Account getAccount(String guid) {
        if (accounts.containsKey(guid))
            return accounts.get(guid);
        return null;
    }

    public Account getAccount(Request req, Response res) {
        String guid = req.params(":guid");
        return getAccount(guid);
    }
}
