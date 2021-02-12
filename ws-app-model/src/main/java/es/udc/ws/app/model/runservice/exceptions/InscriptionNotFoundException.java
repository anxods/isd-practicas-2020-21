package es.udc.ws.app.model.runservice.exceptions;

import java.lang.String;

@SuppressWarnings("serial")
public class InscriptionNotFoundException extends Exception {

    private Long inscriptionId;

    public InscriptionNotFoundException (String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InscriptionNotFoundException (String errorMessage) { super(errorMessage); }

    public InscriptionNotFoundException (Long inscriptionId) {

        super ("The inscription with ID = " + inscriptionId + " ,does not exist.");

        this.inscriptionId = inscriptionId;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

}
