package MoneyMove;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

public class ServerTest {
    @Before
    public void setUp() throws Exception {
        Server newRoutes = new Server();
        newRoutes.establishRoutes();

        awaitInitialization();
    }

    @After
    public void tearDown() throws Exception {
        stop();
    }

    @Test
    public void testRoutes() {
        Account emptyAccount = makeRequest("/create-account/EUR", Account.class);
        Account donator = makeRequest("/create-account/4.16/EUR", Account.class);

        Accounts accounts = makeRequest("/accounts", Accounts.class);
        Assert.assertNotNull(accounts.getAccount(donator.getGuid().toString()));

        MoneyTransfer mt = makeRequest(String.format("/transfer/%s/%s/2.17",
                donator.getGuid().toString(),
                emptyAccount.getGuid().toString()), MoneyTransfer.class);
        Assert.assertTrue(mt.isTransferSuccessful());

        Account firstAccount = makeRequest(String.format("/account/%s", emptyAccount.getGuid().toString()), Account.class);
        Assert.assertEquals(MoneyTest.getDecimal("2.17"), firstAccount.getBalance().getAmount());

        Account secondAccount = makeRequest(String.format("/account/%s", donator.getGuid().toString()), Account.class);
        Assert.assertEquals(MoneyTest.getDecimal("1.99"), secondAccount.getBalance().getAmount());
    }

    public <T> T makeRequest(String path, Class<T> cl) {
        try {
            URL url = new URL("http://localhost:3000" + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            String body = IOUtils.toString(con.getInputStream());
            con.disconnect();

            return new Gson().fromJson(body, cl);
        } catch (IOException e) {
            return null;
        }
    }
}
