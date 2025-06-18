import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import AccidentHistory.AccidentHistory;
import Client.Client;
import Employee.Employee;
import Engine.Engine;
import Enums.CoverageType;
import Enums.EngineType;
import Enums.PaymentSchedule;
import Enums.VehicleType;
import InsuranceContract.InsuranceContract;
import Coverage.Coverage;
import Repository.ContractRepository;
import Repository.ClientRepository;
import Vehicle.Vehicle;

public class App {
    private ContractRepository contractRepository = new ContractRepository("carInsurance/data/contracts");
    private ClientRepository clientRepository = new ClientRepository("carInsurance/data/clients.ser");

    private List<Client> clients = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();

    public App() {
        clients.addAll(clientRepository.loadClients());
        loadSampleVehicles();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().showMainMenu());
    }

    private void showMainMenu() {
        JFrame frame = new JFrame("Asigurare Masini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title label
        JLabel titleLabel = new JLabel("Asigurare Masini", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Buttons with extra spacing
        JButton newContract = new JButton("New Contract");
        JButton clientsBtn = new JButton("Clients");
        JButton paymentBtn = new JButton("Payment");
        JButton contractsBtn = new JButton("Contracts");

        newContract.addActionListener(e -> openNewContractWindow());
        clientsBtn.addActionListener(e -> openClientsWindow());
        paymentBtn.addActionListener(e -> openPaymentWindow());
        contractsBtn.addActionListener(e -> openContractsWindow());

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.add(newContract);
        panel.add(clientsBtn);
        panel.add(paymentBtn);
        panel.add(contractsBtn);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openNewContractWindow() {
        JFrame frame = new JFrame("New Contract");
        JPanel panel = new JPanel(new GridLayout(0, 2));

        JComboBox<Client> clientBox = new JComboBox<>(clients.toArray(new Client[0]));
        JComboBox<Vehicle> modelBox = new JComboBox<>(vehicles.toArray(new Vehicle[0]));
        JTextArea infoArea = new JTextArea(5, 20);
        infoArea.setEditable(false);
        JComboBox<PaymentSchedule> scheduleBox = new JComboBox<>(PaymentSchedule.values());
        JComboBox<String> coverageBox = new JComboBox<>(new String[]{"All", "Accident", "Earthquake", "Fire"});

        modelBox.addActionListener(e -> updateVehicleInfo(modelBox, infoArea));

        panel.add(new JLabel("Client:"));
        panel.add(clientBox);
        panel.add(new JLabel("Model:"));
        panel.add(modelBox);
        panel.add(new JLabel("Info:"));
        panel.add(new JScrollPane(infoArea));
        panel.add(new JLabel("Payment schedule:"));
        panel.add(scheduleBox);
        panel.add(new JLabel("Coverage:"));
        panel.add(coverageBox);


        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e -> handleCreateContract(frame, clientBox, modelBox, scheduleBox, coverageBox));

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(createBtn, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleCreateContract(JFrame parent, JComboBox<Client> clientBox, JComboBox<Vehicle> modelBox,
                                       JComboBox<PaymentSchedule> scheduleBox, JComboBox<String> coverageBox) {
        try {
            Vehicle vehicle = (Vehicle) modelBox.getSelectedItem();
            Client client = (Client) clientBox.getSelectedItem();
            if (vehicle == null || client == null) {
                JOptionPane.showMessageDialog(parent, "Data not selected");
                return;
            }
            Employee emp = new Employee("1", "Agent");
            InsuranceContract contract = emp.createContract(client, vehicle, vehicle.getBasePrice(),
                    (PaymentSchedule) scheduleBox.getSelectedItem());

            String cov = (String) coverageBox.getSelectedItem();
            if ("All".equals(cov) || "Accident".equals(cov)) {
                contract.addCoverage(new Coverage(CoverageType.ACCIDENT, 1.2));
            }
            if ("All".equals(cov) || "Earthquake".equals(cov)) {
                contract.addCoverage(new Coverage(CoverageType.EARTHQUAKE, 1.1));
            }
            if ("All".equals(cov) || "Fire".equals(cov)) {
                contract.addCoverage(new Coverage(CoverageType.FIRE, 1.15));
            }

            contractRepository.saveContract(contract);
            JOptionPane.showMessageDialog(parent, "Contract " + contract.getContractNumber() + " saved");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage());
        }
    }

    private void openClientsWindow() {
        JFrame frame = new JFrame("Clients");
        DefaultListModel<Client> model = new DefaultListModel<>();
        for (Client c : clients) {
            model.addElement(c);
        }
        JList<Client> list = new JList<>(model);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client) {
                    Client client = (Client) value;
                    label.setText(client.getName() + " (" + client.getHistory().getAccidentsLast5Years() + " accidents)");
                }
                return label;
            }
        });

        JLabel accidentInfo = new JLabel("Accidents last 5 years: ");
        list.addListSelectionListener(e -> {
            Client selected = list.getSelectedValue();
            if (selected != null) {
                accidentInfo.setText("Accidents last 5 years: " + selected.getHistory().getAccidentsLast5Years());
            } else {
                accidentInfo.setText("Accidents last 5 years: ");
            }
        });

        JButton add = new JButton("Add Client");
        JButton accident = new JButton("Add Accident");
        JButton delete = new JButton("Delete Client");

        add.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Client name:");
            if (name != null && !name.isEmpty()) {
                Client c = new Client(UUID.randomUUID().toString(), name, new AccidentHistory(0));
                clients.add(c);
                model.addElement(c);
                clientRepository.saveClients(clients);
            }
        });
        accident.addActionListener(e -> {
            Client c = list.getSelectedValue();
            if (c != null) {
                c.getHistory().addAccident();
                list.repaint();
                accidentInfo.setText("Accidents last 5 years: " + c.getHistory().getAccidentsLast5Years());
                clientRepository.saveClients(clients);
            }
        });
        delete.addActionListener(e -> {
            Client c = list.getSelectedValue();
            if (c != null) {
                clients.remove(c);
                model.removeElement(c);
                clientRepository.saveClients(clients);
            }
        });

        JPanel btns = new JPanel();
        btns.add(add);
        btns.add(accident);
        btns.add(delete);
        
        frame.getContentPane().add(accidentInfo, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        frame.getContentPane().add(btns, BorderLayout.SOUTH);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openPaymentWindow() {
        String number = JOptionPane.showInputDialog(null, "Contract number:");
        if (number == null || number.isEmpty()) {
            return;
        }
        InsuranceContract c = contractRepository.findContract(number);
        if (c == null) {
            JOptionPane.showMessageDialog(null, "Contract not found");
            return;
        }
        String amtStr = JOptionPane.showInputDialog(null, "Payment amount:");
        if (amtStr == null || amtStr.isEmpty()) {
            return;
        }
        try {
            double amt = Double.parseDouble(amtStr);
            Employee emp = new Employee("1", "Agent");
            emp.recordPayment(c, amt);
            contractRepository.saveContract(c);
            JOptionPane.showMessageDialog(null, "Payment saved");
        } catch (NumberFormatException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void openContractsWindow() {
        JFrame frame = new JFrame("Contracts");
        DefaultListModel<InsuranceContract> model = new DefaultListModel<>();
        for (InsuranceContract c : contractRepository.loadContracts()) {
            model.addElement(c);
        }
        JList<InsuranceContract> list = new JList<>(model);
        frame.getContentPane().add(new JScrollPane(list));
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
    }

    private void updateVehicleInfo(JComboBox<Vehicle> box, JTextArea area) {
        Vehicle v = (Vehicle) box.getSelectedItem();
        if (v == null) {
            area.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Make: ").append(v.toString()).append('\n');
        sb.append("HP: ").append(v.getHorsepower()).append('\n');
        sb.append("Risk index: ").append(String.format("%.2f", v.getRiskIndex())).append('\n');
        sb.append("Pollution index: ").append(String.format("%.2f", v.getPollutionIndex()));
        area.setText(sb.toString());
    }
}