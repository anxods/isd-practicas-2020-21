package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.Inscription.Inscription;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToRestInscriptionDtoConversor {

    public static RestInscriptionDto toRestInscriptionDto(Inscription inscription) {

        return new RestInscriptionDto(inscription.getInscriptionId(), inscription.getRunId(),
                inscription.getInscriptionDorsal(), inscription.getInscriptionUserEmail(),
                inscription.getInscriptionCreditCardNumber());

    }

    public static Inscription toInscription(RestInscriptionDto restInscription) {

        return new Inscription(restInscription.getInscriptionId(), restInscription.getRunId(),
                restInscription.getInscriptionDorsal(), restInscription.getInscriptionUserEmail(),
                restInscription.getInscriptionCreditCardNumber());

    }

    public static List<RestInscriptionDto> toRestInscriptionDtos(List<Inscription> inscriptions) {

        List<RestInscriptionDto> restInscriptionDtos = new ArrayList<>(inscriptions.size());
        for (int i = 0; i < inscriptions.size(); i++) {
            Inscription inscription = inscriptions.get(i);
            restInscriptionDtos.add(toRestInscriptionDto(inscription));
        }

        return restInscriptionDtos;

    }

}
