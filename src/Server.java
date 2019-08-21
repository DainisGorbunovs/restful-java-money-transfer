import org.apache.log4j.BasicConfigurator;

import static spark.Spark.get;
import static spark.Spark.port;

public class Server {
    public static void main(String[] args) {
        BasicConfigurator.configure();

        port(3000);
        get("/hello", (req, res) -> "Hello World");
    }
}
