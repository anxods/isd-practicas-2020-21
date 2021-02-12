package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientRunService;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.dto.ClientRunDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientInscriptionDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientRunDtoConversor;
import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.RunServiceFactory;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientRunService implements ClientRunService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientRunService.endpointAddress";
    private String endpointAddress;

    @Override
    public ClientRunDto addRun(ClientRunDto run) throws InputValidationException {
        try{

            HttpResponse response = Request.Post(getEndpointAddress() + "runs").bodyStream(
                    toInputStream(run), ContentType.create("application/json")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientRunDtoConversor.toClientRunDto(response.getEntity().getContent());

        } catch (InputValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    // Funcionalidad #2
    @Override
    public ClientRunDto findRun(Long runId) throws InstanceNotFoundException, ClientRunCelebrationException {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "runs/"
                    + runId).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRunDtoConversor.toClientRunDto(
                    response.getEntity().getContent());

        } catch (InstanceNotFoundException | ClientRunCelebrationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Funcionalidad #3
    @Override
    public List<ClientRunDto> findRuns(String city, String date) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress()
                    + "runs?city=" + URLEncoder.encode(city, "UTF-8")
                    + "&date=" + URLEncoder.encode(date, "UTF-8"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRunDtoConversor.toClientRunDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // Funcionalidad #4
    @Override
    public Long registerInRun(Long runId, String inscriptionUserEmail, String inscriptionCreditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientLateInscriptionException, ClientMaxParticipantsException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "inscriptions").
                    bodyForm(
                            Form.form().
                                    add("runId", Long.toString(runId)).
                                    add("inscriptionUserMail", inscriptionUserEmail).
                                    add("inscriptionCreditCardNumber", inscriptionCreditCardNumber).
                                    build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(
                    response.getEntity().getContent()).getInscriptionId();

        } catch (InputValidationException | InstanceNotFoundException | ClientLateInscriptionException | ClientMaxParticipantsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Funcionalidad #5
    @Override
    public List<ClientInscriptionDto> findInscriptions(String mail) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "inscriptions?inscriptionUserEmail="
                + URLEncoder.encode(mail, "UTF-8")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDtos(
                    response.getEntity().getContent()
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    // Funcionalidad #6
    @Override
    public ClientInscriptionDto takeDorsal(Long inscriptionId, String inscriptionCreditCardNumber)
            throws ClientInscriptionNotFoundException, ClientRunCelebrationException, ClientDorsalAlreadyTakenException,
            ClientIncorrectCreditCardException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "inscriptions/dorsals").bodyForm(
                    Form.form().add("inscriptionId", Long.toString(inscriptionId)).
                                add("inscriptionCreditCardNumber", inscriptionCreditCardNumber).
                                build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_ACCEPTED, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(response.getEntity().getContent());

        } catch (ClientInscriptionNotFoundException | ClientRunCelebrationException | ClientDorsalAlreadyTakenException |
                ClientIncorrectCreditCardException ex) {
            throw ex;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientRunDto run) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientRunDtoConversor.toObjectNode(run));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, org.apache.http.HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == successCode || statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
                return;
            }

            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent()
                    );

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent()
                    );

                case HttpStatus.SC_GONE:
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(
                            response.getEntity().getContent()
                    );

                default:
                    throw new RuntimeException("HTTP error; status code = " + statusCode);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
