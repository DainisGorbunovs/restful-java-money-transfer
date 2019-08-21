package MoneyMove;

import com.google.gson.Gson;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        port(3000);

        Gson gson = new Gson();
        Accounts accounts = new Accounts();

        get("/accounts", (req, res) -> accounts, gson::toJson);
        get("/account/:guid", accounts::getAccount, gson::toJson);
        get("/create-account/:amount/:currency", (req, res) -> Account.createAccount(req, res, accounts), gson::toJson);
        get("/transfer/:from/:to/:amount", (req, res) -> MoneyTransfer.transfer(req, res, accounts));

        notFound((req, res) -> {
            res.type("application/json");
            return "{\"status\":404,\"message\":\"Page not found.\"}";
        });
    }
}
