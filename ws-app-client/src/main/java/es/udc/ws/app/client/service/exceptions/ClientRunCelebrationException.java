package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientRunCelebrationException extends Exception {

    Long runId;
    LocalDateTime runStartDate;
    Long inscriptionId;

    public ClientRunCelebrationException (Long runId, LocalDateTime date) {

        super("The run = " + runId +
                " has already taken place the day: " + date
        );

        this.runId = runId;
        this.runStartDate = date;
    }

    public ClientRunCelebrationException(Long inscriptionId) {
        super ("The run for inscription = " + inscriptionId + " has already taken place");

        this.inscriptionId = inscriptionId;
    }

    public ClientRunCelebrationException (String err) {
        super(err);
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
