import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private Money balance;
    private Money.Currency currency;

    public Account(Money.Currency currency) {
        // https://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency
        this(new Money(BigDecimal.ZERO, currency));
    }

    public Account(String balance, Money.Currency currency) {
        this(new Money(balance, currency));
    }

    public Account(Money money) {
        this.balance = money;
    }

    public boolean add(Money amount) {
        return balance.add(amount);
    }

    public boolean subtract(Money amount) {
        try {
            if (balance.compareTo(amount) >= 0) {
                balance.subtract(amount);
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }
}
