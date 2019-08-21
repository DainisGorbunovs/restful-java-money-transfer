# Revolut: money transfers

RESTful API for money transfers.

HTTP paths (`localhost:3000/`):
* `/accounts` - creates currency accounts
* `/account/:guid` - shows info about account 
* `/create-account/:amount/:currency` - creates an account with a specific currency
* `/transfer/:from/:to/:amount` - transfers money between accounts 

Uses framework [`Spark`](http://sparkjava.com/documentation) for receiving and making HTTP requests. Uses [`Gson`](https://github.com/google/gson) for serialising Java objects into JSON.

## IDE support
Requires Lombok:
1. Install Lombok plugin;
2. Enable annotation processing;
3. Restart IntelliJ.

Compile and run tests:
```bash
mvn compile
mvn test
```

## API
The API is hypothetically invoked by internal services and is inaccessible by users.
 

## Money units
There are currencies with more than 2 decimal places.

Examples:
* 2 decimals: 1 EUR = 100 Euro cents
* 3 decimals: 1 BHD = 1000 Bahraini fils
* 8 decimals: 1 satoshi = 0.00000001 BTC
* 3 decimals: 1 millisatoshi = 0.001 satoshi

In this project the currencies are stored up to 12 decimal places using `BigDecimal`. This type avoids using numbers which cannot be represented in binary system. More information on why in -
[Why not use Double or Float to represent currency?](https://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency)