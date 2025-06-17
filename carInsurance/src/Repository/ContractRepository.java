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
        File file = new File(baseDir, contract.getContractNumber() + ".ser");
        contract.saveToFile(file.getAbsolutePath());
        return file;
    }
}
