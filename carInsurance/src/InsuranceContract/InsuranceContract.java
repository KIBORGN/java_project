package InsuranceContract;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Client.Client;
import Coverage.Coverage;
import Payment.Payment;
import Vehicle.Vehicle;
import Enums.PaymentSchedule;

public class InsuranceContract implements Serializable {
    private static final long serialVersionUID = 1L;
    private String contractNumber;
    private LocalDate dateIssued;
    private PaymentSchedule paymentSchedule;
    private Client client;
    private Vehicle vehicle;
    private double insuredSum;
    private double discount;
    private List<Coverage> coverages = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    public InsuranceContract(Client client, Vehicle vehicle, double insuredSum, PaymentSchedule schedule) {
        this.contractNumber = UUID.randomUUID().toString().substring(0, 8);
        this.dateIssued = LocalDate.now();
        this.client = client;
        this.vehicle = vehicle;
        this.insuredSum = insuredSum;
        this.paymentSchedule = schedule;
        switch (schedule) {
            case SEMIANNUAL:
                this.discount = 0.05;
                break;
            case ANNUAL:
                this.discount = 0.10;
                break;
            default:
                this.discount = 0.0;
        }
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getInsuredSum() {
        return insuredSum;
    }

    public double getDiscount() {
        return discount;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void addCoverage(Coverage c) {
        coverages.add(c);
    }

    public void addPayment(Payment p) {
        payments.add(p);
    }

    public double calculatePremium() {
        double base = vehicle.calculateDepreciation() + vehicle.optionsCost();
        base *= vehicle.getRiskIndex();
        base *= vehicle.getPollutionIndex();
        base *= client.getHistory().getRiskFactor();
        for (Coverage c : coverages) {
            base *= c.getRiskFactor();
        }
        return base;
    }

    public double calculateFinalPrice() {
        return calculatePremium() * (1 - discount);
    }

    public void saveToFile(String path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(this);
        }
    }

    public void saveToTextFile(String path) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.println("Contract Number: " + contractNumber);
            out.println("Date Issued: " + dateIssued);
            out.println("Payment Schedule: " + paymentSchedule);
            out.println("Client: " + client.getName());
            out.println("Vehicle: " + vehicle.toString());
            out.println("Insured Sum: " + insuredSum);
            out.println("Discount: " + (int)(discount * 100) + "%");
            out.println("Final Price: " + String.format("%.2f", calculateFinalPrice()));
            for (Coverage c : coverages) {
                out.println("Coverage: " + c.getType() + " factor=" + c.getRiskFactor());
            }
            for (Payment p : payments) {
                out.println("Payment: " + p.getDate() + " amount=" + p.getAmount());
            }
        }
    }

    @Override
    public String toString() {
        return contractNumber + " - " + client.getName() +
                " | Price: " + insuredSum +
                " | Discount: " + (int)(discount * 100) + "%" +
                " | Final Price: " + String.format("%.2f", calculateFinalPrice());
    }
}