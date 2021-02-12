package es.udc.ws.app.model.Run;

import java.time.LocalDateTime;
import java.util.Objects;
import es.udc.ws.app.model.Run.exceptions.*;

public class Run {

    private Long runId;
    private String runLocation;
    private String runDescription;
    private LocalDateTime runStartDate;
    private float runPrice;
    private int runMaxParticipants;
    private int runParticipants;
    private LocalDateTime runCreationDate;

    public Run(String runLocation, String runDescription, LocalDateTime runStartDate,
               float runPrice, int runMaxParticipants, int runParticipants) {
        this.runLocation = runLocation;
        this.runDescription = runDescription;
        this.runStartDate = runStartDate;
        this.runPrice = runPrice;
        this.runMaxParticipants = runMaxParticipants;
        this.runParticipants = runParticipants;
    }

    public Run(Long runId, String runLocation, String runDescription,
               LocalDateTime runStartDate, float runPrice, int runMaxParticipants,
               int runParticipants) {
        this.runId = runId;
        this.runLocation = runLocation;
        this.runDescription = runDescription;
        this.runStartDate = runStartDate;
        this.runPrice = runPrice;
        this.runMaxParticipants = runMaxParticipants;
        this.runParticipants = runParticipants;
    }

    public Run(Long runId, String runLocation, String runDescription,
               LocalDateTime runStartDate, float runPrice, int runMaxParticipants,
               int runParticipants, LocalDateTime runCreationDate) {
        this.runId = runId;
        this.runLocation = runLocation;
        this.runDescription = runDescription;
        this.runStartDate = runStartDate;
        this.runPrice = runPrice;
        this.runMaxParticipants = runMaxParticipants;
        this.runParticipants = runParticipants;
        this.runCreationDate = runCreationDate;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getRunLocation() {
        return runLocation;
    }

    public void setRunLocation(String runLocation) {
        this.runLocation = runLocation;
    }

    public String getRunDescription() {
        return runDescription;
    }

    public void setRunDescription(String runDescription) {
        this.runDescription = runDescription;
    }

    public LocalDateTime getRunStartDate() {
        return runStartDate;
    }

    public void setRunStartDate(LocalDateTime runStartDate) {
        this.runStartDate = runStartDate;
    }

    public float getRunPrice() {
        return runPrice;
    }

    public void setRunPrice(float runPrice) {
        this.runPrice = runPrice;
    }

    public int getRunMaxParticipants() {
        return runMaxParticipants;
    }

    public void setRunMaxParticipants(int runMaxParticipants) {
        this.runMaxParticipants = runMaxParticipants;
    }

    public LocalDateTime getRunCreationDate() {
        return runCreationDate;
    }

    public void setRunCreationDate(LocalDateTime runCreationDate) {
        this.runCreationDate = runCreationDate;
    }

    public int getRunParticipants() {
        return runParticipants;
    }

    public void addParticipant() throws MaxParticipantsException {

        if (this.runParticipants == this.runMaxParticipants) {
            throw new MaxParticipantsException(this.runId);
        } else {
            this.runParticipants += 1;
        }

    }

    public void deleteParticipant() throws NoParticipantsException {

        if (this.runParticipants == 0) {
            throw new NoParticipantsException(this.runId, this.runParticipants);
        } else {
            this.runParticipants -= 1;
        }

    }

    @Override
    public int hashCode() {
        final int prime = 101;
        int result = 1;
        result = prime * result + ((runId == null) ? 0 : runId.hashCode());
        result = prime * result + ((runDescription == null) ? 0 : runDescription.hashCode());
        result = prime * result + ((runLocation == null) ? 0 : runLocation.hashCode());
        result = prime * result + Float.floatToIntBits(runPrice);
        result = prime * result + runMaxParticipants;
        result = prime * result + runParticipants;
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
        Run other = (Run) obj;
        if (runStartDate == null) {
            if (other.runStartDate != null)
                return false;
        } else if (!runStartDate.equals(other.runStartDate))
            return false;
        if (runDescription == null) {
            if (other.runDescription != null)
                return false;
        } else if (!runDescription.equals(other.runDescription))
            return false;
        if (runId == null) {
            if (other.runId != null)
                return false;
        } else if (!runId.equals(other.runId))
            return false;
        if (runMaxParticipants != other.getRunMaxParticipants())
            return false;
        if (runParticipants != other.getRunParticipants())
            return false;
        if (Float.floatToIntBits(runPrice) != Float.floatToIntBits(other.runPrice))
            return false;

        return true;
    }


}
