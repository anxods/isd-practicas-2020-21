package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.dto.ClientRunDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientRunService {

    // Funcionalidad #1
    public ClientRunDto addRun(ClientRunDto run) throws InputValidationException;

    // Funcionalidad #2
    public ClientRunDto findRun(Long runId) throws InstanceNotFoundException, ClientRunCelebrationException;

    // Funcionalidad #3
    public List<ClientRunDto> findRuns(String city, String date);

    // Funcionalidad #4
    public Long registerInRun(Long runId, String inscriptionUserEmail,
                              String inscriptionCreditCardNumber)
            throws InstanceNotFoundException, InputValidationException,
            ClientLateInscriptionException, ClientMaxParticipantsException;

    // Funcionalidad #5
    public List<ClientInscriptionDto> findInscriptions(String mail);

    // Funcionalidad #6
    public ClientInscriptionDto takeDorsal(Long inscriptionId, String inscriptionCreditCardNumber)
            throws ClientInscriptionNotFoundException, ClientRunCelebrationException, ClientDorsalAlreadyTakenException,
            ClientIncorrectCreditCardException;

}
