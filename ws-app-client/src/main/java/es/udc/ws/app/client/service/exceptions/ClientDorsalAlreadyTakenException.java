package es.udc.ws.app.client.service.exceptions;

public class ClientDorsalAlreadyTakenException extends Exception {

    private Long inscriptionId;

    public ClientDorsalAlreadyTakenException(Long inscriptionId) {

        super("The dorsal for the incription = " + inscriptionId +
                " has already been taken."
        );
        this.inscriptionId = inscriptionId;
    }

    public ClientDorsalAlreadyTakenException (String err) {
        super(err);
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }
}
