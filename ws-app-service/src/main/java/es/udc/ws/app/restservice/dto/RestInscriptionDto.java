package es.udc.ws.app.restservice.dto;

public class RestInscriptionDto {

    private Long inscriptionId;
    private Long runId;
    private String inscriptionDorsal;
    private String inscriptionUserEmail;
    private String inscriptionCreditCardNumber;

    public RestInscriptionDto(){

    }

    public RestInscriptionDto(Long inscriptionId, Long runId,
                              String inscriptionDorsal,
                              String inscriptionUserEmail,
                              String inscriptionCreditCardNumber) {
        this.inscriptionId = inscriptionId;
        this.runId = runId;
        this.inscriptionDorsal = inscriptionDorsal;
        this.inscriptionUserEmail = inscriptionUserEmail;
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getInscriptionDorsal() {
        return inscriptionDorsal;
    }

    public void setInscriptionDorsal(String inscriptionDorsal) {
        this.inscriptionDorsal = inscriptionDorsal;
    }

    public String getInscriptionUserEmail() {
        return inscriptionUserEmail;
    }

    public void setInscriptionUserEmail(String inscriptionUserEmail) {
        this.inscriptionUserEmail = inscriptionUserEmail;
    }

    public String getInscriptionCreditCardNumber() {
        return inscriptionCreditCardNumber;
    }

    public void setInscriptionCreditCardNumber(String inscriptionCreditCardNumber) {
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
    }
}