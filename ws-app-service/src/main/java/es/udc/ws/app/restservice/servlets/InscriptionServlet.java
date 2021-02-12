package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.RunService;
import es.udc.ws.app.model.runservice.RunServiceFactory;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.app.restservice.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InscriptionServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String path = ServletUtils.normalizePath(request.getPathInfo());

        if (path == null || path.length() == 0) {
            String userMail = request.getParameter("inscriptionUserEmail");
            RunService runService = RunServiceFactory.getService();
            List<Inscription> inscriptions = null;
            try {
                inscriptions = runService.findInscriptions(userMail);
            } catch (InputValidationException exception) {
                ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInputValidationException(
                        new InputValidationException("Invalid Request: " + "invalid mail")), null);
                return;
            }
            List<RestInscriptionDto> inscriptionDtos = InscriptionToRestInscriptionDtoConversor.toRestInscriptionDtos(inscriptions);
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_OK,
                    JsonToRestInscriptionDtoConversor.toArrayNode(inscriptionDtos), null);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = ServletUtils.normalizePath(request.getPathInfo());

        if (path != null && path.length() > 0){

            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Input Request: " + "invalid path " + path)
                    ), null);
            return;

        }

        String runIdParameter = request.getParameter("runId");

        if (runIdParameter == null) {

            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Input Request: " + "parameter 'runId' is mandatory")
                    ), null);
            return;

        }

        Long runId;

        try {
            runId = Long.valueOf(runIdParameter);
        } catch (NumberFormatException e) {

            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'runId' is invalid '" + runIdParameter + "'")
                    ), null);
            return;

        }

        String inscriptionUserMail = request.getParameter("inscriptionUserMail");

        if (inscriptionUserMail == null) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'inscriptionUserMail' is mandatory")),
                    null);
            return;
        }

        String inscriptionCreditCardNumber = request.getParameter("inscriptionCreditCardNumber");

        if (inscriptionCreditCardNumber == null) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'inscriptionCreditCardNumber' is mandatory")),
                    null);
            return;
        }

        Long inscriptionId = null;

        try {
            RunService service = RunServiceFactory.getService();
            inscriptionId = service.registerInRun(runId, inscriptionUserMail, inscriptionCreditCardNumber);
        } catch (InstanceNotFoundException exception) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInstanceNotFoundException(
                    new InstanceNotFoundException(exception.getInstanceId(), exception.getInstanceType())), null);
            return;
        } catch (MaxParticipantsException e) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toMaxParticipantsException(
                    new MaxParticipantsException(runId)), null);
            return;
        } catch (InputValidationException exception) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInputValidationException(
                    new InputValidationException("Invalid Request: " + "some input isn't valid, check again.")), null);
            return;
        } catch (LateInscriptionException e) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toLateInscriptionException(
                    new LateInscriptionException("Run already occurred, cannot register")), null);
            return;
        }

        RestInscriptionDto restInscriptionDto = new RestInscriptionDto(inscriptionId, runId, inscriptionId.toString(),
                inscriptionUserMail, inscriptionCreditCardNumber);

        ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_CREATED,
                JsonToRestInscriptionDtoConversor.toObjectNode(restInscriptionDto), null);

    }

}
