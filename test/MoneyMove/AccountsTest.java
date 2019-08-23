package MoneyMove;

import org.junit.Assert;
import org.junit.Test;
import spark.Request;
import spark.Response;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

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
    }

    @Test
    public void getAccount() {
        Accounts accounts = new Accounts();
        Account firstAccount = new Account("GBP");
        accounts.addAccount(firstAccount);

        Account foundAccount = accounts.getAccount(firstAccount.getGuid().toString());
        Assert.assertEquals(firstAccount.getBalance(), foundAccount.getBalance());
        Assert.assertEquals(firstAccount.getGuid(), foundAccount.getGuid());
    }

    @Test
    public void getAccountViaRequest() {
        Accounts accounts = new Accounts();
        Account firstAccount = new Account("GBP");
        accounts.addAccount(firstAccount);

        // TODO Request is a protected class, avoid mocking it (and Response)
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        when(request.params(":guid")).thenReturn(firstAccount.getGuid().toString());

        Account foundAccount = accounts.getAccount(request, response);
        Assert.assertEquals(firstAccount.getBalance(), foundAccount.getBalance());
        Assert.assertEquals(firstAccount.getGuid(), foundAccount.getGuid());
    }
}
