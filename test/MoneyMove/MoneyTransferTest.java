package MoneyMove;

import org.junit.*;

public class MoneyTransferTest {
    @Test
    public void testPrecision() {
        Account a = new Account(new Money("1.03", "EUR"));
        Account b = new Account(new Money("0.00", "EUR"));

        Assert.assertTrue(MoneyTransfer.transfer(a, b, "0.42"));

        Assert.assertEquals(new Money("0.61", "EUR"), a.getBalance());
        Assert.assertEquals(new Money("0.42", "EUR"), b.getBalance());
    }

    @Test
    public void testTransferSignatures() {
        Account a = new Account(new Money("1.03", "EUR"));
        Account b = new Account(new Money("0.00", "EUR"));
        Assert.assertTrue(MoneyTransfer.transfer(a, b, "0.42", "EUR"));

        Assert.assertFalse(MoneyTransfer.transfer(a, b, "0.42", "GBP"));
        Assert.assertFalse(MoneyTransfer.transfer(a, b, "0.42", Money.Currency.GBP));
    }

    @Test
    public void testFailedTransfers() {
        Account a = new Account(new Money("1.03", "GBP"));
        Account b = new Account(new Money("0.00", "EUR"));
        Account c = new Account(new Money("2.00", "EUR"));

        Assert.assertFalse(MoneyTransfer.transfer(null, b, "0.42", "EUR"));
        Assert.assertFalse(MoneyTransfer.transfer(b, null, "0.42", "EUR"));


        Assert.assertFalse(MoneyTransfer.transfer(a, b, "0.42", "EUR"));
        Assert.assertFalse(MoneyTransfer.transfer(b, c, "0.42", "GBP"));
    }
}
