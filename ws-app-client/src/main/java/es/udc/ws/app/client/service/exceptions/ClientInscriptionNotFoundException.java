package es.udc.ws.app.client.service.exceptions;

public class ClientInscriptionNotFoundException extends Exception {

    Long inscriptionId;

    public ClientInscriptionNotFoundException (Long inscriptionId) {
        super("The inscription = " + inscriptionId +
                " was not found.");

        this.inscriptionId = inscriptionId;
    }

    public ClientInscriptionNotFoundException (String err) {
        super(err);
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

}
