package es.udc.ws.app.model.runservice.exceptions;

import java.lang.String;

@SuppressWarnings("serial")
public class DorsalAlreadyTakenException extends Exception {

    private Long inscriptionId;

    public DorsalAlreadyTakenException (String errorMessage) {
        super(errorMessage);
    }

    public DorsalAlreadyTakenException (Long inscriptionId) {

        super("The dorsal for the inscription : "
                + inscriptionId + " has already been taken");

        this.inscriptionId = inscriptionId;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }
}
