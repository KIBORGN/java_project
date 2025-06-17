package Repository;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import InsuranceContract.InsuranceContract;

public class ContractRepository {
    private File baseDir;

    public ContractRepository(String baseDir) {
        this.baseDir = new File(baseDir);
        if (!this.baseDir.exists()) {
            this.baseDir.mkdirs();
        }
    }

    public File saveContract(InsuranceContract contract) throws IOException {
        File serFile = new File(baseDir, contract.getContractNumber() + ".ser");
        contract.saveToFile(serFile.getAbsolutePath());
        File txtFile = new File(baseDir, contract.getContractNumber() + ".txt");
        contract.saveToTextFile(txtFile.getAbsolutePath());
        return serFile;
    }

    public List<InsuranceContract> loadContracts() {
        List<InsuranceContract> list = new ArrayList<>();
        File[] files = baseDir.listFiles((d, n) -> n.endsWith(".ser"));
        if (files != null) {
            for (File f : files) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
                    list.add((InsuranceContract) in.readObject());
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
        }
        return list;
    }

    public InsuranceContract findContract(String number) {
        for (InsuranceContract c : loadContracts()) {
            if (c.getContractNumber().equals(number)) {
                return c;
            }
        }
        return null;
    }
}