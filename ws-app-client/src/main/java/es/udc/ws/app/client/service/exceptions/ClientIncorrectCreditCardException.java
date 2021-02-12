package es.udc.ws.app.client.service.exceptions;

public class ClientIncorrectCreditCardException extends Exception {

    private Long inscriptionId;
    private String inscriptionCreditCardNumber;

    public ClientIncorrectCreditCardException (Long inscriptionId, String inscriptionCreditCardNumber) {
        super("The credit card with number = " + inscriptionCreditCardNumber +
                " does not match with the inscription = " + inscriptionId +
                " in our database."
        );

        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
        this.inscriptionId = inscriptionId;
    }

    public ClientIncorrectCreditCardException (String err) {
        super(err);
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public String getInscriptionCreditCardNumber() {
        return inscriptionCreditCardNumber;
    }

    public void setInscriptionCreditCardNumber(String inscriptionCreditCardNumber) {
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
    }
}
