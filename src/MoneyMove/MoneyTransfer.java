package MoneyMove;

import spark.Request;
import spark.Response;

public class MoneyTransfer {
    public MoneyTransfer() {

    }

    public static boolean transfer(Account a, Account b, String amount) {
        // if currency is omitted, it is read from first account
        // if second account has a different currency, it is caught in next transfer()
        return transfer(a, b, new Money(amount, a.getCurrency()));
    }

    public static boolean transfer(Account a, Account b, String amount, String currency) {
        return transfer(a, b, new Money(amount, currency));
    }

    public static boolean transfer(Account a, Account b, String amount, Money.Currency currency) {
        return transfer(a, b, new Money(amount, currency));
    }

    public static boolean transfer(Account a, Account b, Money amount) {
        // if the accounts are not found
        if (a == null || b == null)
            return false;

        // if currencies don't match
        if (a.getCurrency() != b.getCurrency() || a.getCurrency() != amount.getCurrency())
            return false;

        // proceed only if removed money from A
        if (!a.subtract(amount))
            return false;

        return b.add(amount);
    }


    public static boolean transfer(Request req, Response res, Accounts accounts) {
        String from = req.params(":from");
        String to = req.params(":to");
        String amount = req.params(":amount");

        Account fromAccount = accounts.getAccount(from);
        Account toAccount = accounts.getAccount(to);
        return transfer(fromAccount, toAccount, amount);
    }
}
