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
import InsuranceContract.InsuranceContract;
import Repository.ContractRepository;
import Vehicle.Vehicle;

public class App {
    private JTextField clientNameField = new JTextField(15);
    private JTextField makeField = new JTextField(10);
    private JTextField modelField = new JTextField(10);
    private JTextField yearField = new JTextField(4);
    private JComboBox<EngineType> engineBox = new JComboBox<>(EngineType.values());
    private JComboBox<PaymentSchedule> scheduleBox = new JComboBox<>(PaymentSchedule.values());
    private ContractRepository repository = new ContractRepository("carInsurance/data/contracts");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("E-Asigurari masini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Client name:"));
        panel.add(clientNameField);
        panel.add(new JLabel("Make:"));
        panel.add(makeField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Engine:"));
        panel.add(engineBox);
        panel.add(new JLabel("Payment schedule:"));
        panel.add(scheduleBox);

        JButton createButton = new JButton("Create Contract");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCreateContract(frame);
            }
        });

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(createButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void handleCreateContract(JFrame parent) {
        try {
            Employee emp = new Employee("1", "Agent");
            AccidentHistory history = new AccidentHistory(0);
            Client client = new Client("100", clientNameField.getText(), history);
            Engine engine = new Engine((EngineType) engineBox.getSelectedItem(), "Euro6");
            int year = Integer.parseInt(yearField.getText());
            Vehicle vehicle = new Vehicle("VIN" + System.currentTimeMillis(),
                    makeField.getText(), modelField.getText(), year, 10000, engine);
            InsuranceContract contract = emp.createContract(client, vehicle, vehicle.getBasePrice(),
                    (PaymentSchedule) scheduleBox.getSelectedItem());

            File path = repository.saveContract(contract);
            JOptionPane.showMessageDialog(parent, "Contract saved to " + path.getAbsolutePath());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Invalid year");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error saving contract: " + ex.getMessage());
        }
    }
}
