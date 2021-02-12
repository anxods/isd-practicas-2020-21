package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.thrift.ThriftInscriptionDto;

import java.util.ArrayList;
import java.util.List;

public class ClientInscriptionDtoToThriftInscriptionDtoConversor {

    public static ThriftInscriptionDto toThriftInscriptionDto(ClientInscriptionDto inscriptionDto) {

        Long inscriptionId = inscriptionDto.getInscriptionId();

        return new ThriftInscriptionDto(inscriptionId == null ? -1 : inscriptionId.longValue(),
                inscriptionDto.getRunId(),
                inscriptionDto.getInscriptionDorsal(),
                inscriptionDto.getInscriptionUserEmail(),
                inscriptionDto.getInscriptionCreditCardNumber()
        );

    }

    public static List<ClientInscriptionDto> toClientInscriptionDto(List<ThriftInscriptionDto> inscriptionDtos) {

        List<ClientInscriptionDto> clientInscriptionDtos = new ArrayList<>(inscriptionDtos.size());

        for (ThriftInscriptionDto inscriptionDto : inscriptionDtos) {
            clientInscriptionDtos.add(toClientInscriptionDto(inscriptionDto));
        }

        return clientInscriptionDtos;

    }

    public static ClientInscriptionDto toClientInscriptionDto(ThriftInscriptionDto inscriptionDto) {

        return new ClientInscriptionDto(
                inscriptionDto.getInscriptionId(),
                inscriptionDto.getRunId(),
                inscriptionDto.getInscriptionDorsal(),
                inscriptionDto.getInscriptionUserEmail(),
                inscriptionDto.getInscriptionCreditCardNumber()
        );

    }

}
