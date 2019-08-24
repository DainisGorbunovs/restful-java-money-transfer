package MoneyMove;

import org.junit.Assert;
import org.junit.Test;
import spark.Request;

import java.math.BigDecimal;

import static MoneyMove.Money.currencyPrecision;
import static MoneyMove.Money.roundingMode;

public class AccountTest {
    public BigDecimal getDecimal(String amount) {
        return new BigDecimal(amount).setScale(currencyPrecision, roundingMode);
    }

    @Test
    public void createWithMoney() {
        Account account = new Account("EUR");
        Assert.assertEquals(Money.Currency.EUR, account.getCurrency());

        account = new Account(Money.Currency.GBP);
        Assert.assertEquals(Money.Currency.GBP, account.getCurrency());

        account = new Account("13.01", Money.Currency.BTC);
        Assert.assertEquals(Money.Currency.BTC, account.getCurrency());
        Assert.assertEquals(getDecimal("13.01"), account.getBalance().getAmount());

        account = new Account("13.01", "BTC");
        Assert.assertEquals(Money.Currency.BTC, account.getCurrency());
        Assert.assertEquals(getDecimal("13.01"), account.getBalance().getAmount());

        account = new Account(new Money("EUR"));
        Assert.assertEquals(Money.Currency.EUR, account.getCurrency());
    }

    @Test
    public void createAccount() {
        Request request = new Request() {
            @Override
            public String params(String a) {
                if (a.equals(":amount"))
                    return "12.023282";
                return "BTC";
            }
        };
        Accounts accounts = new Accounts();

        Account account = Account.createAccount(request, null, accounts);
        Assert.assertEquals(getDecimal("12.023282"), account.getBalance().getAmount());

        Accounts accounts2 = new Accounts() {
            private boolean answer = false;

            @Override
            boolean addAccount(Account account) {
                if (answer)
                    return true;
                this.answer = true;
                return false;
            }
        };

        Account other = Account.createAccount(request, null, accounts2);
        Assert.assertNotEquals(account.getGuid(), other.getGuid());
    }

    @Test
    public void testAdd() {
        Account account = new Account("EUR");
        Assert.assertFalse(account.add(new Money("0.01", "GBP")));
        Assert.assertEquals(BigDecimal.ZERO, account.getBalance().getAmount());
        Assert.assertTrue(account.add(new Money("0.01", "EUR")));
        Assert.assertEquals(getDecimal("0.01"), account.getBalance().getAmount());
    }

    @Test
    public void testSubtract() {
        Account account = new Account("EUR");
        Assert.assertFalse(account.subtract(new Money("0.01", "GBP")));
        Assert.assertEquals(BigDecimal.ZERO, account.getBalance().getAmount());
        Assert.assertTrue(account.subtract(new Money("0.01", "EUR")));
        Assert.assertEquals(getDecimal("-0.01"), account.getBalance().getAmount());
    }
}
