package MoneyMove;

import org.junit.Assert;
import org.junit.Test;
import spark.Request;

public class AccountsTest {
    @Test
    public void createAccounts() {
        Accounts accounts = new Accounts();
        Account firstAccount = new Account("GBP");
        Assert.assertTrue(accounts.addAccount(firstAccount));

        Account secondAccount = new Account("EUR");
        secondAccount.setGuid(firstAccount.getGuid());
        Assert.assertFalse(accounts.addAccount(secondAccount));
        Assert.assertEquals(1, accounts.getCount());

        Account thirdAccount = new Account("BTC");
        Assert.assertTrue(accounts.addAccount(thirdAccount));
        Assert.assertEquals(2, accounts.getCount());
        Assert.assertEquals(2, accounts.getAccounts().size());
    }

    @Test
    public void getAccount() {
        Accounts accounts = new Accounts();
        Account firstAccount = new Account("GBP");
        accounts.addAccount(firstAccount);

        Account foundAccount = accounts.getAccount(firstAccount.getGuid().toString());
        Assert.assertEquals(firstAccount.getBalance(), foundAccount.getBalance());
        Assert.assertEquals(firstAccount.getGuid(), foundAccount.getGuid());

        Assert.assertNull(accounts.getAccount(null));
    }

    @Test
    public void getAccountViaRequest() {
        Accounts accounts = new Accounts();
        Account firstAccount = new Account("GBP");
        accounts.addAccount(firstAccount);

        Request request = new Request() {
            @Override
            public String params(String a) {
                return firstAccount.getGuid().toString();
            }
        };

        Account foundAccount = accounts.getAccount(request, null);
        Assert.assertEquals(firstAccount.getBalance(), foundAccount.getBalance());
        Assert.assertEquals(firstAccount.getGuid(), foundAccount.getGuid());
    }
}
