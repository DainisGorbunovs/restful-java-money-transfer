public class MoneyTransfer {
    public MoneyTransfer() {

    }

    public static boolean transfer(Account a, Account b, String amount) {
        return transfer(a, b, new Money(amount));
    }

    public static boolean transfer(Account a, Account b, String amount, Money.Currency currency) {
        return transfer(a, b, new Money(amount, currency));
    }

    public static boolean transfer(Account a, Account b, Money amount) {
        // proceed only if removed money from A
        if (!a.subtract(amount))
            return false;

        return b.add(amount);
    }
}
