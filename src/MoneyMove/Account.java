package MoneyMove;

import lombok.Data;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Account {
    private UUID guid;
    private Money balance;
    private Money.Currency currency;

    public Account(String currency) {
        this(new Money(BigDecimal.ZERO, currency));
    }

    public Account(Money.Currency currency) {
        this(new Money(BigDecimal.ZERO, currency));
    }

    public Account(String balance, Money.Currency currency) {
        this(new Money(balance, currency));
    }

    public Account(String balance, String currency) {
        this(new Money(balance, currency));
    }

    Account(Money money) {
        this.setBalance(money);
        this.setCurrency(money.getCurrency());
        this.guid = UUID.randomUUID();
    }

    static Account createAccount(Request req, Response res, Accounts accounts) {
        String amount = req.params(":amount");
        String currency = req.params(":currency");

        Account account;
        if (amount == null)
            account = new Account(currency);
        else
            account = new Account(amount, currency);

        // if a duplicate GUID, generate a new one
        while (!accounts.addAccount(account)) {
            account.setGuid(UUID.randomUUID());
        }

        return account;
    }

    boolean add(Money amount) {
        if (balance.getCurrency() == amount.getCurrency())
            return balance.add(amount);
        return false;
    }

    boolean subtract(Money amount) {
        if (balance.getCurrency() == amount.getCurrency()) {
            return balance.subtract(amount);
        }
        return false;
    }
}
