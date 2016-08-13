package Server;

import java.math.BigDecimal;

/**
 * Created by mahsa on 07/08/2016.
 */
public class Deposit {

    String customer;
    String id;
    BigDecimal initialBalance;
    BigDecimal upperBound;

    public Deposit(String customer, String id, BigDecimal initialBalance, BigDecimal upperBound) {
        this.customer = customer;
        this.id = id;
        this.initialBalance = initialBalance;
        this.upperBound = upperBound;
    }

    public  BigDecimal depositOpetation(BigDecimal amount) {
        initialBalance = initialBalance.add(amount);
        return initialBalance;
    }

    public BigDecimal withdrawOperation(BigDecimal amount) {
        initialBalance = initialBalance.subtract(amount);
        return initialBalance;
    }
}
