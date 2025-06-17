package Engine;

import java.io.Serializable;

import Enums.EngineType;

public class Engine implements Serializable {
    private static final long serialVersionUID = 1L;
    private EngineType type;
    private String euroClass;

    public Engine(EngineType type, String euroClass) {
        this.type = type;
        this.euroClass = euroClass;
    }

    public EngineType getType() {
        return type;
    }

    public String getEuroClass() {
        return euroClass;
    }

    public double getPollutionIndex(int vehicleAge) {
        double base = 1.0;
        switch (type) {
            case DIESEL:
                base = 1.2;
                break;
            case PETROL:
                base = 1.0;
                break;
            case ELECTRIC:
                base = 0.2;
                break;
        }

        double euroFactor = 1.0;
        String cls = euroClass.toLowerCase();
        if (cls.startsWith("euro")) {
            try {
                int num = Integer.parseInt(cls.substring(4));
                euroFactor = 1.3 - Math.min(num, 6) * 0.1;
            } catch (NumberFormatException ignore) {
                euroFactor = 1.0;
            }
        }

        double ageFactor = 1 + Math.min(vehicleAge, 20) * 0.02;
        return base * euroFactor * ageFactor;
    }

    public double getPollutionIndex() {
        return getPollutionIndex(0);
    }
}