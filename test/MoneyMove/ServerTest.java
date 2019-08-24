package MoneyMove;

import com.google.gson.Gson;
import lombok.Data;
import org.junit.*;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

public class ServerTest {
    @BeforeClass
    public static void setUp() {
        Server.main(new String[]{});

        awaitInitialization();
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Test
    public void testRoutes() {
        StatusMessage<Account> emptyAccountMessage = makeRequest("/create-account/EUR", Account.class);
        Account emptyAccount = emptyAccountMessage.getMessage();
        StatusMessage<Account> donatorMessage = makeRequest("/create-account/4.16/EUR", Account.class);
        Account donator = donatorMessage.getMessage();

        StatusMessage<Accounts> accountsMessage = makeRequest("/accounts", Accounts.class);
        Accounts accounts = accountsMessage.getMessage();

        Assert.assertNotNull(accounts.getAccount(
                donator.getGuid().toString()
        ));

        StatusMessage<MoneyTransfer> mt = makeRequest(String.format("/transfer/%s/%s/2.17",
                donator.getGuid().toString(),
                emptyAccount.getGuid().toString()), MoneyTransfer.class);
        Assert.assertTrue(mt.getMessage().isTransferSuccessful());

        StatusMessage<Account> firstAccountMessage = makeRequest(String.format("/account/%s",
                emptyAccount.getGuid().toString()), Account.class);
        Account firstAccount = firstAccountMessage.getMessage();
        Assert.assertEquals(MoneyTest.getDecimal("2.17"), firstAccount.getBalance().getAmount());

        StatusMessage<Account> secondAccountMessage = makeRequest(String.format("/account/%s",
                donator.getGuid().toString()), Account.class);
        Account secondAccount = secondAccountMessage.getMessage();
        Assert.assertEquals(MoneyTest.getDecimal("1.99"), secondAccount.getBalance().getAmount());
    }

    @Test
    public void testNotFound() {
        StatusMessage<String> msg = makeRequest("/nonexistent", String.class, true);
        Assert.assertEquals(404, msg.getStatus());
    }

    @Data
    public class StatusMessage<T> {
        int status;
        T message;
    }

    private <T> StatusMessage<T> makeRequest(String path, Class<T> cl) {
        return makeRequest(path, cl, false);
    }

    private <T> StatusMessage<T> makeRequest(String path, Class<T> cl, boolean getErrorStream) {
        try {
            URL url = new URL("http://localhost:3000" + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            String body;

            if (getErrorStream) {
                body = IOUtils.toString(con.getErrorStream());
            } else {
                body = IOUtils.toString(con.getInputStream());
            }
            con.disconnect();

            StatusMessage<T> msg = new StatusMessage<>();
            msg.setStatus(status);

            if (cl.equals(String.class))
                msg.setMessage(cl.cast(body));
            else
                msg.setMessage(new Gson().fromJson(body, cl));
            return msg;
        } catch (IOException e) {
            return null;
        }
    }
}
