package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientRunService;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;

import es.udc.ws.app.client.service.dto.ClientRunDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.exceptions.*;

import es.udc.ws.app.thrift.*;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class ThriftClientRunService implements ClientRunService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientRunService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    // Funcionalidad #1
    @Override
    public ClientRunDto addRun(ClientRunDto run) throws InputValidationException{

        ThriftRunService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();

            return ClientRunDtoToThriftRunDtoConversor.toClientRunDto(client.addRun(ClientRunDtoToThriftRunDtoConversor.toThriftRunDto(run)));

        } catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        } catch (TException e){
            e.printStackTrace();
        } finally {
            transport.close();
        }

        return null;
    }


    // Funcionalidad #2
    @Override
    public ClientRunDto findRun(Long runId) throws InstanceNotFoundException, ClientRunCelebrationException {

        ThriftRunService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();

            return ClientRunDtoToThriftRunDtoConversor.toClientRunDto(client.findRun(runId));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftRunCelebrationException e) {
            throw new ClientRunCelebrationException(e.getMessage());
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }

        return null;
    }

    // Funcionalidad #3
    @Override
    public List<ClientRunDto> findRuns(String city, String date) {

        ThriftRunService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();

            return ClientRunDtoToThriftRunDtoConversor.toClientRunDto(client.findRuns(city, date));

        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }

        return null;

    }

    // Funcionalidad #4
    @Override
    public Long registerInRun(Long runId, String inscriptionUserEmail, String inscriptionCreditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientLateInscriptionException, ClientMaxParticipantsException {

        ThriftRunService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();

            return client.registerInRun(runId, inscriptionUserEmail, inscriptionCreditCardNumber);

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftLateInscriptionException e) {
            throw new ClientLateInscriptionException(e.getMessage());
        } catch (ThriftMaxParticipantsException e) {
            throw new ClientMaxParticipantsException(e.getMessage());
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }

        // Sin ponerle esto me dice que no hay return statement
        return runId;

    }

    // Funcionalidad #5
    @Override
    public List<ClientInscriptionDto> findInscriptions(String keywords) {

        ThriftRunService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();

            return ClientInscriptionDtoToThriftInscriptionDtoConversor.toClientInscriptionDto(client.findInscriptions(keywords));

        } catch (TException e) {
            e.printStackTrace();
        }

        // Sin ponerle esto me dice que no hay return statement
        return null;
    }


    // Funcionalidad #6
    @Override
    public ClientInscriptionDto takeDorsal(Long inscriptionId, String inscriptionCreditCardNumber) throws
            ClientInscriptionNotFoundException, ClientRunCelebrationException, ClientDorsalAlreadyTakenException,
            ClientIncorrectCreditCardException {

        ThriftRunService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();

            return ClientInscriptionDtoToThriftInscriptionDtoConversor.toClientInscriptionDto(
                    client.takeDorsal(inscriptionId, inscriptionCreditCardNumber));

        } catch (ThriftInscriptionNotFoundException e) {
            throw new ClientInscriptionNotFoundException(e.getMessage());
        } catch (ThriftRunCelebrationException e) {
            throw new ClientRunCelebrationException(e.getMessage());
        } catch (ThriftDorsalAlreadyTakenException e) {
            throw new ClientDorsalAlreadyTakenException(e.getMessage());
        } catch (ThriftIncorrectCreditCardException e) {
            throw new ClientIncorrectCreditCardException(e.getMessage());
        } catch (ThriftRuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }

        return null;
    }

    private ThriftRunService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftRunService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}
