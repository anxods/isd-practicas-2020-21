package es.udc.ws.app.client.service.exceptions;

public class ClientNoParticipantsException extends Exception {

    private Long runId;
    private int runParticipants;

    public ClientNoParticipantsException (Long runId, int runParticipants) {
        super("Run with id=\"" + runId +
                "\" has no participants yet (runParticipants = \"" +
                runParticipants + "\")");
        this.runId = runId;
        this.runParticipants = runParticipants;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public int getRunParticipants() {
        return runParticipants;
    }

}
