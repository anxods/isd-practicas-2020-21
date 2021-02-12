package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientRunDto;
import es.udc.ws.app.thrift.ThriftRunDto;

import java.util.ArrayList;
import java.util.List;

public class ClientRunDtoToThriftRunDtoConversor {

    public static ThriftRunDto toThriftRunDto(ClientRunDto clientRunDto) {

        Long runId = clientRunDto.getRunId();

        return new ThriftRunDto(runId == null ? -1 : runId.longValue(),
                clientRunDto.getRunLocation(),
                clientRunDto.getRunDescription(),
                clientRunDto.getRunStartDate(),
                clientRunDto.getRunPrice(),
                clientRunDto.getRunMaxParticipants(),
                clientRunDto.getRunParticipants());

    }

    public static List<ClientRunDto> toClientRunDto(List<ThriftRunDto> runs) {

        List<ClientRunDto> clientRunDtos = new ArrayList<>(runs.size());

        for (ThriftRunDto runDto : runs) {
            clientRunDtos.add(toClientRunDto(runDto));
        }

        return clientRunDtos;

    }

    public static ClientRunDto toClientRunDto(ThriftRunDto run) {

        return new ClientRunDto(
                run.getRunId(),
                run.getRunLocation(),
                run.getRunDescription(),
                run.getRunStartDate(),
                (float) run.getRunPrice(),
                run.getRunMaxParticipants(),
                run.getRunParticipants()
        );

    }

}
