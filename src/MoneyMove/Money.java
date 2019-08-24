package MoneyMove;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Money {
    public static int currencyPrecision = 12;
    public static RoundingMode roundingMode = RoundingMode.HALF_UP;
    private final Currency currency;
    private BigDecimal amount;

    public Money(String amount, String currency) {
        this(new BigDecimal(amount).setScale(currencyPrecision, roundingMode), Currency.valueOf(currency));
    }

    public Money(String amount, Currency currency) {
        this(new BigDecimal(amount).setScale(currencyPrecision, roundingMode), currency);
    }

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = Currency.valueOf(currency);
    }

    public Money(Currency currency) {
        this.amount = BigDecimal.ZERO;
        this.currency = currency;
    }

    public Money(String currency) {
        this(Currency.valueOf(currency));
    }

    boolean add(Money money) {
        if (this.getCurrency() != money.getCurrency())
            return false;

        this.amount = this.amount.add(money.getAmount());
        return true;
    }

    boolean subtract(Money money) {
        if (this.getCurrency() != money.getCurrency())
            return false;

        // allow to go into debt, and thus have a negative value
        this.amount = this.amount.subtract(money.getAmount());
        return true;
    }

    int compareTo(Money money) throws Exception {
        if (this.getCurrency() != money.getCurrency())
            throw new Exception("Unable to compare different currencies.");

        return this.getAmount().compareTo(money.getAmount());
    }

    public enum Currency {
        BTC,
        GBP,
        EUR,
        USD
    }
}
