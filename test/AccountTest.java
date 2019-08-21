import org.junit.Assert;
import org.junit.Test;


public class AccountTest {
    @Test
    public void testPrecision() {
        Account a = new Account(new Money("1.03"));
        Account b = new Account(new Money("0.00"));

        MoneyTransfer.transfer(a, b,"0.42");

        Assert.assertEquals(a.getBalance(), new Money("0.61"));
        Assert.assertEquals(b.getBalance(), new Money("0.42"));
    }
}
