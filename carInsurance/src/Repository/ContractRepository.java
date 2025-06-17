package Repository;

import java.io.File;
import java.io.IOException;

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
}
