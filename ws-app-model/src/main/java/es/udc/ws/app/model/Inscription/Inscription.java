package es.udc.ws.app.model.Inscription;

import java.time.LocalDateTime;

public class Inscription {

    private Long inscriptionId;
    private Long runId;
    private String inscriptionDorsal;
    private String inscriptionUserEmail;
    private String inscriptionCreditCardNumber;
    private LocalDateTime inscriptionDate;
    private boolean isDorsalTaken;

    public Inscription(Long runId, String inscriptionDorsal, String inscriptionUserEmail,
                       String inscriptionCreditCardNumber, LocalDateTime inscriptionDate, boolean isDorsalTaken) {
        this.runId = runId;
        this.inscriptionDorsal = inscriptionDorsal;
        this.inscriptionUserEmail = inscriptionUserEmail;
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
        this.inscriptionDate = inscriptionDate;
        this.isDorsalTaken = isDorsalTaken;
    }

    public Inscription(Long inscriptionId, Long runId, String inscriptionDorsal,
                       String inscriptionUserEmail, String inscriptionCreditCardNumber) {
        this.inscriptionId = inscriptionId;
        this.runId = runId;
        this.inscriptionDorsal = inscriptionDorsal;
        this.inscriptionUserEmail = inscriptionUserEmail;
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
    }

    public Inscription(Long inscriptionId, Long runId, String inscriptionDorsal,
                       String inscriptionUserEmail, String inscriptionCreditCardNumber,
                       LocalDateTime inscriptionDate) {
        this.inscriptionId = inscriptionId;
        this.runId = runId;
        this.inscriptionDorsal = inscriptionDorsal;
        this.inscriptionUserEmail = inscriptionUserEmail;
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
        this.inscriptionDate = inscriptionDate;
    }

    public Inscription(Long inscriptionId, Long runId, String inscriptionDorsal,
                       String inscriptionUserEmail, String inscriptionCreditCardNumber,
                       LocalDateTime inscriptionDate, boolean isDorsalTaken) {
        this.inscriptionId = inscriptionId;
        this.runId = runId;
        this.inscriptionDorsal = inscriptionDorsal;
        this.inscriptionUserEmail = inscriptionUserEmail;
        this.inscriptionCreditCardNumber = inscriptionCreditCardNumber;
        this.inscriptionDate = inscriptionDate;
        this.isDorsalTaken = isDorsalTaken;
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

    public LocalDateTime getInscriptionDate() {
        return inscriptionDate;
    }

    public void setInscriptionDate(LocalDateTime inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }

    public boolean isDorsalTaken() { return this.isDorsalTaken; }

    public void setIsDorsalTaken (boolean isDorsalTaken) { this.isDorsalTaken = isDorsalTaken; }

    @Override
    public int hashCode() {
        final int prime = 101;
        int result = 1;
        result = prime * result + ((inscriptionId == null) ? 0 : inscriptionId.hashCode());
        result = prime * result + ((runId == null) ? 0 : runId.hashCode());
        result = prime * result + ((inscriptionDorsal == null) ? 0 : inscriptionDorsal.hashCode());
        result = prime * result + ((inscriptionUserEmail == null) ? 0 : inscriptionUserEmail.hashCode());
        result = prime * result + ((inscriptionCreditCardNumber == null) ? 0 : inscriptionCreditCardNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Inscription other = (Inscription) obj;
        if (inscriptionId == null) {
            if (other.inscriptionId != null)
                return false;
        } else if (!inscriptionId.equals(other.inscriptionId))
            return false;
        if (runId == null) {
            if (other.runId != null)
                return false;
        } else if (!runId.equals(other.runId))
            return false;
        if (inscriptionDate == null) {
            if (other.inscriptionDate != null)
                return false;
        } else if (!inscriptionDate.equals(other.inscriptionDate))
            return false;
        if (inscriptionDorsal == null) {
            if (other.inscriptionDate != null)
                return false;
        } else if (!inscriptionDorsal.equals(other.inscriptionDorsal))
            return false;
        if (inscriptionUserEmail == null) {
            if (other.inscriptionUserEmail != null)
                return false;
        } else if (!inscriptionUserEmail.equals(other.inscriptionUserEmail))
            return false;
        if (inscriptionCreditCardNumber == null) {
            if (other.inscriptionCreditCardNumber != null)
                return false;
        } else if (!inscriptionCreditCardNumber.equals(other.inscriptionCreditCardNumber))
            return false;
        if (isDorsalTaken == false) {
            if (other.isDorsalTaken != false)
                return false;
        } else if (!other.isDorsalTaken)
            return false;
        return true;
    }

}
