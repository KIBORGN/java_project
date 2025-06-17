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

    public double getPollutionIndex() {
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
        return base;
    }
}
