package Payment;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String paymentId;
    private LocalDate date;
    private double amount;

    public Payment(double amount) {
        this.paymentId = UUID.randomUUID().toString();
        this.date = LocalDate.now();
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
