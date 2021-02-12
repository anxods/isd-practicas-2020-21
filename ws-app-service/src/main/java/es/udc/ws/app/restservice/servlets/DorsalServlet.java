package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.model.runservice.RunServiceFactory;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.app.restservice.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DorsalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = ServletUtils.normalizePath(request.getPathInfo());

        if (path != null && path.length() > 0) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }
        String inscriptionIdAsString = request.getParameter("inscriptionId");
        Long inscriptionId;
        String inscriptionCreditCardNumber = request.getParameter("inscriptionCreditCardNumber");
        try {
            inscriptionId = Long.valueOf(inscriptionIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInputValidationException(
                    new InputValidationException("Invalid Request: " + "invalid inscription id '" + inscriptionIdAsString)), null);
            return;
        }
        Inscription inscription;
        try {
            inscription = RunServiceFactory.getService().takeDorsal(inscriptionId, inscriptionCreditCardNumber);
        } catch (RunCelebrationException e) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toRunCelebrationException(
                    new RunCelebrationException(e.getMessage())), null);
            return;
        } catch (DorsalAlreadyTakenException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toDorsalAlreadyTakenException(
                    new DorsalAlreadyTakenException(ex.getMessage())), null);
            return;
        } catch (IncorrectCreditCardException e) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND, JsonToExceptionConversor.toIncorrectCreditCardException(
                    new IncorrectCreditCardException(e.getMessage())), null);
            return;
        } catch (InscriptionNotFoundException ex) {
            ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND, JsonToExceptionConversor.toInscriptionNotFoundException(
                    new InscriptionNotFoundException(ex.getMessage())), null);
            return;
        }
        RestInscriptionDto inscriptionDto = InscriptionToRestInscriptionDtoConversor.toRestInscriptionDto(inscription);

        ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_ACCEPTED,
                JsonToRestInscriptionDtoConversor.toObjectNode(inscriptionDto), null);
    }

}
