package AccidentHistory;

import java.io.Serializable;

public class AccidentHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private int accidentsLast5Years;

    public AccidentHistory(int accidentsLast5Years) {
        this.accidentsLast5Years = accidentsLast5Years;
    }

    public int getAccidentsLast5Years() {
        return accidentsLast5Years;
    }

    public double getRiskFactor() {
        if (accidentsLast5Years <= 0) {
            return 1.0;
        }
        return 1.0 + accidentsLast5Years * 0.1;
    }
}
