package MoneyMove;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Money {
    public static int currencyPrecision = 12;
    public static RoundingMode roundmode = RoundingMode.HALF_UP;
    private BigDecimal amount;
    private final Currency currency;

    public enum Currency {
        BTC,
        GBP,
        EUR,
        USD
    }

    public Money(String amount, String currency) {
        this(new BigDecimal(amount).setScale(currencyPrecision, roundmode), Currency.valueOf(currency));
    }

    public Money(String amount, Currency currency) {
        this(new BigDecimal(amount).setScale(currencyPrecision, roundmode), currency);
    }

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public boolean add(Money money) {
        if (this.getCurrency() != money.getCurrency())
            return false;

        this.amount = this.amount.add(money.getAmount());
        return true;
    }

    public boolean subtract(Money money) {
        if (this.getCurrency() != money.getCurrency())
            return false;

        // allow to go into debt, and thus have a negative value
        this.amount = this.amount.subtract(money.getAmount());
        return true;
    }

    public int compareTo(Money money) throws Exception {
        if (this.getCurrency() != money.getCurrency())
            throw new Exception("Unable to compare different currencies.");

        return this.getAmount().compareTo(money.getAmount());
    }
}
