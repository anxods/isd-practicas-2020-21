package es.udc.ws.app.model.runservice.exceptions;

import java.time.LocalDateTime;
import java.lang.String;

@SuppressWarnings("serial")
public class RunCelebrationException extends Exception {

    private Long runId;
    private LocalDateTime runStartDate;
    private Long inscriptionId;

    public RunCelebrationException(String errorMessage) {
        super(errorMessage);
    }

    public RunCelebrationException(Long runId, LocalDateTime date) {
        super("Run with id=\"" + runId +
                "\" has taken place (celebrationDate = \"" +
                date+ "\")");

        this.runId = runId;
        this.runStartDate = date;
    }

    public RunCelebrationException(Long inscriptionId) {
        super("Run for inscription = " + inscriptionId +
                " has already taken place");
        this.inscriptionId = inscriptionId;
    }

    public RunCelebrationException (String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public LocalDateTime getRunStartDate() {
        return runStartDate;
    }

    public void setRunStartDate(LocalDateTime runStartDate) {
        this.runStartDate = runStartDate;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }
}
