package es.udc.ws.app.model.Run.exceptions;

import java.time.LocalDateTime;

public class MaxParticipantsException extends Exception {

    private Long runId;

    public MaxParticipantsException(Long runId) {
        super("Run with id=\"" + runId +
                "\" is already full");
        this.runId = runId;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

}
