package es.udc.ws.app.client.service.exceptions;

public class ClientMaxParticipantsException extends Exception {

    private Long runId;
    private int runParticipants;
    private int runMaxParticipants;

    public ClientMaxParticipantsException (Long runId, int runParticipants, int runMaxParticipants) {
        super("Run with id=\"" + runId +
                "\" is already full (" + runParticipants + " participants over " +
                 runMaxParticipants + "maximum of participants");
        this.runId = runId;
        this.runParticipants = runParticipants;
        this.runMaxParticipants = runMaxParticipants;
    }

    public ClientMaxParticipantsException (String err) {
        super(err);
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public int getRunParticipants() { return runParticipants; }

    public void setRunParticipants(int runParticipants) { this.runParticipants = runParticipants; }

    public int getRunMaxParticipants() { return runMaxParticipants; }

    public void setRunMaxParticipants(int runMaxParticipants) { this.runMaxParticipants = runMaxParticipants; }
}
