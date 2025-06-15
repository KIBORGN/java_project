import javax.swing.*;
import java.awt.event.*;

import AccidentHistory.AccidentHistory;
import Client.Client;
import Employee.Employee;
import Engine.Engine;
import Enums.EngineType;
import Enums.PaymentSchedule;
import Vehicle.Vehicle;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("E-Asigurari masini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Create Contract");

        // anonymous inner class for event handling
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSampleContract();
                JOptionPane.showMessageDialog(frame, "Contract created (demo)");
            }
        });

        frame.getContentPane().add(button);
        frame.pack();
        frame.setVisible(true);
    }

    private void performSampleContract() {
        Employee emp = new Employee("1", "John");
        AccidentHistory history = new AccidentHistory(0);
        Client client = new Client("100", "Client A", history);
        Engine engine = new Engine(EngineType.PETROL, "Euro6");
        Vehicle vehicle = new Vehicle("VIN123", "Make", "Model", 2020, 10000, engine);
        emp.createContract(client, vehicle, 12000, PaymentSchedule.ANNUAL);
    }
}
