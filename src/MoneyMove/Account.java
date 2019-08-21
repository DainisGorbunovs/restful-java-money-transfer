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

    public Account(Money.Currency currency) {
        this(new Money(BigDecimal.ZERO, currency));
    }

    public Account(String balance, Money.Currency currency) {
        this(new Money(balance, currency));
    }

    public Account(String balance, String currency) {
        this(new Money(balance, currency));
    }

    public Account(Money money) {
        this.setBalance(money);
        this.setCurrency(money.getCurrency());
        this.guid = UUID.randomUUID();
    }

    public static Account createAccount(Request req, Response res, Accounts accounts) {
        String amount = req.params(":amount");
        String currency = req.params(":currency");

        Account account = new Account(amount, currency);
        // if a duplicate GUID, generate a new one
        while (!accounts.addAccount(account)) {
            account.setGuid(UUID.randomUUID());
        }

        return account;
    }

    public boolean add(Money amount) {
        if (balance.getCurrency() == amount.getCurrency())
            return balance.add(amount);
        return false;
    }

    public boolean subtract(Money amount) {
        try {
            if (balance.getCurrency() == amount.getCurrency() && balance.compareTo(amount) >= 0) {
                balance.subtract(amount);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}
