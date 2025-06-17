package Employee;

import Client.Client;
import InsuranceContract.InsuranceContract;
import Vehicle.Vehicle;
import Enums.PaymentSchedule;

public class Employee {
    private String employeeId;
    private String name;

    public Employee(String employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
    }

    public InsuranceContract createContract(Client client, Vehicle vehicle, double insuredSum, PaymentSchedule schedule) {
        return new InsuranceContract(client, vehicle, insuredSum, schedule);
    }

    public void recordPayment(InsuranceContract contract, double amount) {
        contract.addPayment(new Payment.Payment(amount));
    }
}
