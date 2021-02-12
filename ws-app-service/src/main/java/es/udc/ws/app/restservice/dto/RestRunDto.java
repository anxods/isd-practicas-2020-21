package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestRunDto {

    private Long runId;
    private String runLocation;
    private String runDescription;
    private String runStartDate;
    private float runPrice;
    private int runMaxParticipants;
    private int runParticipants;

    public RestRunDto() {

    }

    public RestRunDto(Long runId, String runLocation, String runDescription,
                      String runStartDate, float runPrice,
                      int runMaxParticipants, int runParticipants) {
        this.runId = runId;
        this.runLocation = runLocation;
        this.runDescription = runDescription;
        this.runStartDate = runStartDate;
        this.runPrice = runPrice;
        this.runMaxParticipants = runMaxParticipants;
        this.runParticipants = runParticipants;
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

    public String getRunStartDate() {
        return runStartDate;
    }

    public void setRunStartDate(String runStartDate) {
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

    public int getRunParticipants() {
        return runParticipants;
    }

    public void setRunParticipants(int runParticipants) {
        this.runParticipants = runParticipants;
    }

    @Override
    public String toString() {
        return "Run [runId = " + runId + ", runLocation = " + runLocation
                + ", runDescription = " + runDescription
                + ", runStartDate = " + runStartDate + "]";
    }

}