package es.udc.ws.app.model.runservice;

import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.model.runservice.exceptions.*;

import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

public interface RunService {

    // Funcionalidad #1
    public Run addRun(Run run) throws InputValidationException;

    // Funcionalidad #2
    public Run findRun(Long runId) throws InstanceNotFoundException, RunCelebrationException;

    // Funcionalidad #3
    public List<Run> findRuns(String city, String date);

    // Funcionalidad #4
    public Long registerInRun(Long runId, String inscriptionUserEmail,
                                     String inscriptionCreditCardNumber)
        throws InstanceNotFoundException, InputValidationException,
            LateInscriptionException, MaxParticipantsException;

    // Funcionalidad #5
    public List<Inscription> findInscriptions(String keywords) throws InputValidationException;

    // Funcionalidad #6
    public Inscription takeDorsal(Long inscriptionId, String inscriptionCreditCardNumber)
            throws RuntimeException, InscriptionNotFoundException, RunCelebrationException,
            DorsalAlreadyTakenException, IncorrectCreditCardException;

}
