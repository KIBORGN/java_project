package Client;

import java.io.Serializable;

import AccidentHistory.AccidentHistory;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientId;
    private String name;
    private AccidentHistory accidentHistory;

    public Client(String clientId, String name, AccidentHistory history) {
        this.clientId = clientId;
        this.name = name;
        this.accidentHistory = history;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public AccidentHistory getHistory() {
        return accidentHistory;
    }
}
