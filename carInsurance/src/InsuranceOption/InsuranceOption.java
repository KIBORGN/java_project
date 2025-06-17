package InsuranceOption;

import java.io.Serializable;

public class InsuranceOption implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private double cost;

    public InsuranceOption(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}
