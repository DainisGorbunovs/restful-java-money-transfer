package MoneyMove;

import lombok.Data;
import spark.Request;
import spark.Response;

@Data
public class MoneyTransfer {
    private final boolean transferSuccessful;

    private MoneyTransfer(boolean success) {
        this.transferSuccessful = success;
    }

    static boolean transfer(Account a, Account b, String amount) {
        // if currency is omitted, it is read from first account
        // if second account has a different currency, it is caught in next transfer()
        return transfer(a, b, new Money(amount, a.getCurrency()));
    }

    static boolean transfer(Account a, Account b, String amount, String currency) {
        return transfer(a, b, new Money(amount, currency));
    }

    static boolean transfer(Account a, Account b, String amount, Money.Currency currency) {
        return transfer(a, b, new Money(amount, currency));
    }

    static boolean transfer(Account a, Account b, Money amount) {
        // if the accounts are not found
        if (a == null || b == null)
            return false;

        // if currencies don't match
        if (a.getCurrency() != b.getCurrency() || a.getCurrency() != amount.getCurrency())
            return false;

        // the currencies are matched, thus can make the transfer
        a.subtract(amount);
        return b.add(amount);
    }


    static MoneyTransfer transfer(Request req, Response res, Accounts accounts) {
        String from = req.params(":from");
        String to = req.params(":to");
        String amount = req.params(":amount");

        Account fromAccount = accounts.getAccount(from);
        Account toAccount = accounts.getAccount(to);

        boolean status = transfer(fromAccount, toAccount, amount);

        return new MoneyTransfer(status);
    }
}
