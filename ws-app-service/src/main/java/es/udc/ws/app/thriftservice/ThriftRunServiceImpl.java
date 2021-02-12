package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.RunServiceFactory;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.util.List;

public class ThriftRunServiceImpl implements ThriftRunService.Iface {

    // Funcionalidad #1
    @Override
    public ThriftRunDto addRun(ThriftRunDto runDto) throws ThriftInputValidationException {

        Run run = RunToThriftRunDtoConversor.toRun(runDto);

        try{
            return RunToThriftRunDtoConversor.toThriftRunDto(RunServiceFactory.getService().addRun(run));
        } catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    // Funcionalidad #2
    @Override
    public ThriftRunDto findRun(long runId) throws ThriftInstanceNotFoundException, ThriftRunCelebrationException,
            TException {
        try {

            return RunToThriftRunDtoConversor.toThriftRunDto(RunServiceFactory.getService().findRun(runId));

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1)
            );
        } catch (RunCelebrationException e) {
            throw new ThriftRunCelebrationException(e.getMessage());
        }
    }

    // Funcionalidad #3
    @Override
    public List<ThriftRunDto> findRuns(String city, String date) throws TException {
        List<Run> runs = RunServiceFactory.getService().findRuns(city, date);

        return RunToThriftRunDtoConversor.toThriftRunDtos(runs);
    }

    // Funcionalidad #4
    @Override
    public long registerInRun(long runId, String inscriptionUserEmail, String inscriptionCreditCardNumber)
            throws ThriftInstanceNotFoundException, ThriftInputValidationException, ThriftLateInscriptionException, ThriftMaxParticipantsException, TException {
        try {

            return RunServiceFactory.getService().registerInRun(runId, inscriptionUserEmail, inscriptionCreditCardNumber);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1)
            );
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (LateInscriptionException e) {
            throw new ThriftLateInscriptionException(e.getMessage());
        } catch (MaxParticipantsException e) {
            throw new ThriftMaxParticipantsException(e.getRunId());
        }
    }

    // Funcionalidad #5
    @Override
    public List<ThriftInscriptionDto> findInscriptions(String keywords) throws TException, ThriftLateInscriptionException {
        try{
            List<Inscription> inscriptions = RunServiceFactory.getService().findInscriptions(keywords);
            return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDtos(inscriptions);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    // Funcionalidad #6
    @Override
    public ThriftInscriptionDto takeDorsal(long inscriptionId, String inscriptionCreditCardNumber) throws ThriftRuntimeException,
            ThriftInscriptionNotFoundException, ThriftRunCelebrationException, ThriftDorsalAlreadyTakenException,
            ThriftIncorrectCreditCardException, TException {
        try {

            return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDto(
                    RunServiceFactory.getService().takeDorsal(inscriptionId, inscriptionCreditCardNumber));

        } catch (RuntimeException e) {
            throw new ThriftRuntimeException(e.getMessage());
        } catch (InscriptionNotFoundException e) {
            throw new ThriftInscriptionNotFoundException(e.getMessage());
        } catch (RunCelebrationException e) {
            throw new ThriftRunCelebrationException(e.getMessage());
        } catch (DorsalAlreadyTakenException e) {
            throw new ThriftDorsalAlreadyTakenException(e.getMessage());
        } catch (IncorrectCreditCardException e) {
            throw new ThriftIncorrectCreditCardException(e.getMessage());
        }
    }
}
