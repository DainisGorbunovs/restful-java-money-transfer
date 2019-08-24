package MoneyMove;

import com.google.gson.Gson;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        new Server().establishRoutes();
    }

    private void establishRoutes() {
        BasicConfigurator.configure();
        Gson gson = new Gson();
        Accounts accounts = new Accounts();
        port(3000);

        get("/accounts", (req, res) -> accounts, gson::toJson);
        get("/account/:guid", accounts::getAccount, gson::toJson);
        get("/create-account/:amount/:currency", (req, res) -> Account.createAccount(req, res, accounts), gson::toJson);
        get("/create-account/:currency", (req, res) -> Account.createAccount(req, res, accounts), gson::toJson);
        get("/transfer/:from/:to/:amount", (req, res) -> MoneyTransfer.transfer(req, res, accounts), gson::toJson);
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
    }
}
