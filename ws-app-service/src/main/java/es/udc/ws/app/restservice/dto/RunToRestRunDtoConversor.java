package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.Run.Run;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RunToRestRunDtoConversor {

    public static List<RestRunDto> toRestRunDtos(List<Run> runs) {
        List<RestRunDto> runDtos = new ArrayList<>(runs.size());
        for (int i = 0; i < runs.size(); i++) {
            Run run = runs.get(i);
            runDtos.add(toRestRunDto(run));
        }
        return runDtos;
    }

    public static RestRunDto toRestRunDto(Run run) {
        return new RestRunDto(run.getRunId(), run.getRunLocation(),
                run.getRunDescription(), run.getRunStartDate().toString(),
                run.getRunPrice(), run.getRunMaxParticipants(),
                run.getRunParticipants());
    }

    public static Run toRun(RestRunDto run) {
        return new Run(run.getRunId(), run.getRunLocation(),
                run.getRunDescription(), LocalDateTime.parse(run.getRunStartDate()),
                run.getRunPrice(), run.getRunMaxParticipants(),
                run.getRunParticipants());
    }

}
