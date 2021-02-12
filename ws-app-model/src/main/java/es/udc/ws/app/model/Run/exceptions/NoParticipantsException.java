package es.udc.ws.app.model.Run.exceptions;

public class NoParticipantsException extends Exception {

    private Long runId;
    private int runParticipants;

    public NoParticipantsException(Long runId, int runParticipants) {
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
