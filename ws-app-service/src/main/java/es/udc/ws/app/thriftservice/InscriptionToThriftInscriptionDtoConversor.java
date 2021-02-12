package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.thrift.ThriftInscriptionDto;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToThriftInscriptionDtoConversor {

    public static List<ThriftInscriptionDto> toThriftInscriptionDtos(List<Inscription> inscriptions) {

        List<ThriftInscriptionDto> thriftInscriptionDtos = new ArrayList<>(inscriptions.size());

        for (Inscription inscription : inscriptions) {
            thriftInscriptionDtos.add(toThriftInscriptionDto(inscription));
        }

        return thriftInscriptionDtos;

    }

    public static ThriftInscriptionDto toThriftInscriptionDto(Inscription inscription) {
        return new ThriftInscriptionDto(inscription.getInscriptionId(), inscription.getRunId(),
                inscription.getInscriptionDorsal(), inscription.getInscriptionUserEmail(), inscription.getInscriptionCreditCardNumber());
    }

}
