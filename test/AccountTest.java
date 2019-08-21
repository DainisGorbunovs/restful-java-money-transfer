import org.junit.Assert;
import org.junit.Test;


public class AccountTest {
    @Test
    public void testPrecision() {
        Account a = new Account(new Money("1.03", "EUR"));
        Account b = new Account(new Money("0.00", "EUR"));

        Assert.assertTrue(MoneyTransfer.transfer(a, b, "0.42"));

        Assert.assertEquals(new Money("0.61", "EUR"), a.getBalance());
        Assert.assertEquals(new Money("0.42", "EUR"), b.getBalance());
    }
}
