package Coverage;

import java.io.Serializable;

import Enums.CoverageType;

public class Coverage implements Serializable {
    private static final long serialVersionUID = 1L;
    private CoverageType type;
    private double riskFactor;

    public Coverage(CoverageType type, double riskFactor) {
        this.type = type;
        this.riskFactor = riskFactor;
    }

    public CoverageType getType() {
        return type;
    }

    public double getRiskFactor() {
        return riskFactor;
    }
}
