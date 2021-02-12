package es.udc.ws.app.model.runservice.exceptions;

import java.lang.String;

@SuppressWarnings("serial")
public class IncorrectCreditCardException extends Exception {

    private Long inscriptionId;
    private String inscriptionCreditCardNumber;

    public IncorrectCreditCardException (String errorMessage) {
        super(errorMessage);
    }

    public IncorrectCreditCardException (Long inscriptionId, String inscriptionCreditCardNumber) {

        super("The credit card = "
                + inscriptionCreditCardNumber
                + " does not match with the used during the inscription of inscription = " + inscriptionId);

        this.inscriptionId = inscriptionId;
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
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