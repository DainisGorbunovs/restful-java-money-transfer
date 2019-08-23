package MoneyMove;

import lombok.Data;
import spark.Request;
import spark.Response;

import java.util.HashMap;

@Data
public class Accounts {
    private HashMap<String, Account> accounts;

    public Accounts() {
        accounts = new HashMap<>();
    }

    boolean addAccount(Account account) {
        String guid = account.getGuid().toString();
        if (accounts.containsKey(guid))
            return false;

        accounts.put(guid, account);
        return true;
    }

    Account getAccount(String guid) {
        if (accounts.containsKey(guid))
            return accounts.get(guid);
        return null;
    }

    Account getAccount(Request req, Response res) {
        String guid = req.params(":guid");
        return getAccount(guid);
    }

    int getCount() {
        return this.accounts.size();
    }
}
