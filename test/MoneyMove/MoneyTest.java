package MoneyMove;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static MoneyMove.Money.currencyPrecision;
import static MoneyMove.Money.roundingMode;

public class MoneyTest {
    public static BigDecimal getDecimal(String amount) {
        return new BigDecimal(amount).setScale(currencyPrecision, roundingMode);
    }

    @Test
    public void testStringMoneyCurrency() {
        Money money = new Money("12.34", "USD");
        Assert.assertEquals(getDecimal("12.34"), money.getAmount());
        Assert.assertEquals("USD", money.getCurrency().toString());
    }

    @Test
    public void testStringMoneyEnumCurrency() {
        Money money = new Money("12.34", Money.Currency.GBP);
        Assert.assertEquals(Money.Currency.GBP, money.getCurrency());
    }

    @Test
    public void testDecimalMoneyCurrency() {
        Money money = new Money(getDecimal("23.90"), "EUR");
        Assert.assertEquals(getDecimal("23.90"), money.getAmount());
    }

    @Test
    public void testZeroMoney() {
        Money money = new Money("GBP");
        Assert.assertEquals(BigDecimal.ZERO, money.getAmount());

        money = new Money(Money.Currency.BTC);
        Assert.assertEquals(BigDecimal.ZERO, money.getAmount());
    }

    @Test
    public void testAdd() {
        Money nothing = new Money("EUR");
        Money wrongCurrency = new Money("1.00", "USD");

        Assert.assertFalse(nothing.add(wrongCurrency));
        Assert.assertEquals(BigDecimal.ZERO, nothing.getAmount());

        Money someMoney = new Money("1.00", "EUR");
        Assert.assertTrue(nothing.add(someMoney));
        Assert.assertEquals(getDecimal("1.00"), nothing.getAmount());
    }

    @Test
    public void testSubtract() {
        Money nothing = new Money("EUR");
        Money wrongCurrency = new Money("1.00", "USD");

        Assert.assertFalse(nothing.subtract(wrongCurrency));
        Assert.assertEquals(BigDecimal.ZERO, nothing.getAmount());

        Money someMoney = new Money("1.00", "EUR");
        Assert.assertTrue(nothing.subtract(someMoney));
        Assert.assertEquals(getDecimal("-1.00"), nothing.getAmount());
    }

    @Test
    public void testCompareTo() {
        Money some = new Money("1.00", "EUR");
        Money wrongCurrency = new Money("1.00", "USD");

        try {
            some.compareTo(wrongCurrency);
            Assert.fail("The currencies are supposed to be different.");
        } catch(Exception ignored) {}

        Money more = new Money("2.34", "EUR");
        try {
            Assert.assertEquals(1, more.compareTo(some));
            Assert.assertEquals(-1, some.compareTo(more));
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
