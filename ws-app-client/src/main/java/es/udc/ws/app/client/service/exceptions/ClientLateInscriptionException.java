package es.udc.ws.app.client.service.exceptions;

public class ClientLateInscriptionException extends Exception {

    Long runId;


    public ClientLateInscriptionException (Long runId) {

        super("The incriptions for the run = " + runId +
                " are already close."
        );

        this.runId = runId;
    }

    public ClientLateInscriptionException (String err) {
        super(err);
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }
}
