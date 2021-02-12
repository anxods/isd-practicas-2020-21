package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.thrift.ThriftRunDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RunToThriftRunDtoConversor {

    public static Run toRun(ThriftRunDto run) {
        return new Run(run.getRunId(), run.getRunLocation(), run.getRunDescription(),
                LocalDateTime.parse(run.getRunStartDate()), (float)run.getRunPrice(), run.getRunMaxParticipants(),
                run.getRunParticipants());
    }

    public static List<ThriftRunDto> toThriftRunDtos(List<Run> runs) {

        List<ThriftRunDto> dtos = new ArrayList<>(runs.size());

        for (Run run : runs) {
            dtos.add(toThriftRunDto(run));
        }

        return dtos;
    }

    public static ThriftRunDto toThriftRunDto(Run run) {
        return new ThriftRunDto(run.getRunId(), run.getRunLocation(),
                run.getRunDescription(), run.getRunStartDate().toString(), run.getRunPrice(),
                run.getRunMaxParticipants(), run.getRunParticipants());
    }

}
