package AccidentHistory;

import java.io.Serializable;

public class AccidentHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private int accidentsLast5Years;

    // Constructor ce seteaza numarul de accidente din ultimii 5 ani
    public AccidentHistory(int accidentsLast5Years) {
        this.accidentsLast5Years = accidentsLast5Years;
    }

    // Intoarce numarul de accidente din ultimii 5 ani
    public int getAccidentsLast5Years() {
        return accidentsLast5Years;
    }

    // Incrementeaza numarul de accidente inregistrate
    public void addAccident() {
        accidentsLast5Years++;
    }

    // Calculeaza factorul de risc in functie de accidente (1 + nr * 0.1)
    public double getRiskFactor() {
        if (accidentsLast5Years <= 0) {
            return 1.0;
        }
        return 1.0 + accidentsLast5Years * 0.1;
    }
}