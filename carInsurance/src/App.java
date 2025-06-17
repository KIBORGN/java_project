import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import AccidentHistory.AccidentHistory;
import Client.Client;
import Employee.Employee;
import Engine.Engine;
import Enums.EngineType;
import Enums.PaymentSchedule;
import Enums.VehicleType;
import InsuranceContract.InsuranceContract;
import Repository.ContractRepository;
import Vehicle.Vehicle;

public class App {
    private JTextField clientNameField = new JTextField(15);
    private JComboBox<Vehicle> modelBox = new JComboBox<>();
    private JTextArea infoArea = new JTextArea(5, 20);
    private JComboBox<PaymentSchedule> scheduleBox = new JComboBox<>(PaymentSchedule.values());
    private ContractRepository repository = new ContractRepository("carInsurance/data/contracts");
    private java.util.List<Vehicle> vehicles = new java.util.ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("E-Asigurari masini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadSampleVehicles();

        JPanel panel = new JPanel(new GridLayout(0, 2));

        panel.add(new JLabel("Client name:"));
        panel.add(clientNameField);
        panel.add(new JLabel("Select model:"));
        panel.add(modelBox);
        panel.add(new JLabel("Vehicle info:"));
        infoArea.setEditable(false);
        panel.add(new JScrollPane(infoArea));
        panel.add(new JLabel("Payment schedule:"));
        panel.add(scheduleBox);

        modelBox.addActionListener(e -> updateVehicleInfo());

        JButton createButton = new JButton("Create Contract");
        createButton.addActionListener(e -> handleCreateContract(frame));

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(createButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleCreateContract(JFrame parent) {
        try {
            Vehicle selected = (Vehicle) modelBox.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(parent, "No vehicle selected");
                return;
            }

            Employee emp = new Employee("1", "Agent");
            AccidentHistory history = new AccidentHistory(0);
            Client client = new Client("100", clientNameField.getText(), history);
            // Use the selected vehicle directly
            Vehicle vehicle = selected;
            InsuranceContract contract = emp.createContract(
                    client,
                    vehicle,
                    vehicle.getBasePrice(),
                    (PaymentSchedule) scheduleBox.getSelectedItem()
            );

            File path = repository.saveContract(contract);
            File txtPath = new File(path.getParentFile(), contract.getContractNumber() + ".txt");
            JOptionPane.showMessageDialog(parent,
                    "Contract saved to " + path.getAbsolutePath() + " and " + txtPath.getAbsolutePath());
            JOptionPane.showMessageDialog(parent, "Contract saved to " + path.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error saving contract: " + ex.getMessage());
        }
    }

    private void loadSampleVehicles() {
        int currentYear = java.time.Year.now().getValue();
        vehicles.add(new Vehicle("VIN1", "Ford", "Focus", currentYear,
                15000, new Engine(EngineType.PETROL, "Euro6"), 120, VehicleType.CAR));
        vehicles.add(new Vehicle("VIN2", "Yamaha", "MT-07", currentYear,
                9000, new Engine(EngineType.PETROL, "Euro5"), 75, VehicleType.MOTORCYCLE));
        vehicles.add(new Vehicle("VIN3", "BMW", "X5", currentYear,
                45000, new Engine(EngineType.DIESEL, "Euro6"), 250, VehicleType.CAR));
        vehicles.add(new Vehicle("VIN4", "Classic", "Carriage", currentYear,
                2000, new Engine(EngineType.PETROL, "Euro4"), 50, VehicleType.CARRIAGE));

        for (Vehicle v : vehicles) {
            modelBox.addItem(v);
        }
    }

    private void updateVehicleInfo() {
        Vehicle v = (Vehicle) modelBox.getSelectedItem();
        if (v == null) {
            infoArea.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Make: ").append(v.toString()).append('\n');
        sb.append("HP: ").append(v.getHorsepower()).append('\n');
        sb.append("Risk index: ").append(String.format("%.2f", v.getRiskIndex())).append('\n');
        sb.append("Pollution index: ").append(String.format("%.2f", v.getPollutionIndex()));
        infoArea.setText(sb.toString());
    }

    // Sample method for demo purposes
    private void performSampleContract(JFrame parent) {
        Employee emp = new Employee("1", "John");
        AccidentHistory history = new AccidentHistory(0);
        Client client = new Client("100", "Client A", history);
        Engine engine = new Engine(EngineType.PETROL, "Euro6");
        Vehicle vehicle = new Vehicle("VIN123", "Make", "Model", 2020, 10000,
                engine, 150, VehicleType.CAR);
        InsuranceContract contract = emp.createContract(client, vehicle, 12000, PaymentSchedule.ANNUAL);
        try {
            File path = repository.saveContract(contract);
            JOptionPane.showMessageDialog(parent, "Sample contract saved to " + path.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error saving sample contract: " + ex.getMessage());
        }
    }
}