package Client;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by mahsa on 07/08/2016.
 */
public class Transaction implements Serializable {
    private String id;
    private String type;
    private BigDecimal amount;
    private String depositNumber;
    private String terminalId;

    public Transaction(String id, String type, BigDecimal amount, String depositNumber, String terminalId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.depositNumber = depositNumber;
        this.terminalId = terminalId;

    }

    public void setId(String transactionId) {
        id = transactionId;
    }

    public String getId() {
        return id;
    }

    public void setType(String transactionType) {
        type = transactionType;
    }

    public String getType() {
        return type;
    }

    public void setAmount(BigDecimal transactionAmount) {
        amount = transactionAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setDeposit(String transactionDepositNumber) {
        depositNumber = transactionDepositNumber;
    }

    public void setTerminalId(String terminallId) {
        terminalId = terminallId;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public String getTerminalId() {
        return terminalId;
    }
}
