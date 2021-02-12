package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.runservice.RunServiceFactory;
import es.udc.ws.app.model.runservice.exceptions.RunCelebrationException;
import es.udc.ws.app.restservice.dto.RestRunDto;
import es.udc.ws.app.restservice.dto.RunToRestRunDtoConversor;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestRunDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String path = ServletUtils.normalizePath(request.getPathInfo());

        if (path == null || path.length() == 0){ // escri (findRuns por ciudad y fecha)
            String city = request.getParameter("city");
            String date = request.getParameter("date");
            List<Run> runs = RunServiceFactory.getService().findRuns(city, date);
            List<RestRunDto> runDtos = RunToRestRunDtoConversor.toRestRunDtos(runs);
            ServletUtils.writeServiceResponse(
                    response, HttpServletResponse.SC_OK,
                    JsonToRestRunDtoConversor.toArrayNode(runDtos), null
            );
        } else { // adri (findRun por id)
            String runIdAsString = path.substring(1);
            Long runId;

            try {
                runId = Long.valueOf(runIdAsString);
            } catch (NumberFormatException e) {
                ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(
                                new InputValidationException("Invalid Request: " + "invalid runId #" + runIdAsString)
                        ), null);
                return;
            }

            Run run;

            try {
                run = RunServiceFactory.getService().findRun(runId);
            } catch (RunCelebrationException e) {
                ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_GONE,
                        JsonToExceptionConversor.toRunCelebrationException(e),
                        null);
                return;
            } catch (InstanceNotFoundException exception) {
                ServletUtils.writeServiceResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        JsonToExceptionConversor.toInstanceNotFoundException(exception), null);
                return;
            }

            RestRunDto runDto;
            runDto = RunToRestRunDtoConversor.toRestRunDto(run);
            ServletUtils.writeServiceResponse(
                    response, HttpServletResponse.SC_OK,
                    JsonToRestRunDtoConversor.toObjectNode(runDto), null
            );

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String path = ServletUtils.normalizePath(request.getPathInfo());

        if (path != null && path.length() > 0) {
            ServletUtils.writeServiceResponse(
                    response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)
                    ), null
            );
            return;
        }

        RestRunDto runDto;

        try {
            runDto = JsonToRestRunDtoConversor.toServiceRunDto(request.getInputStream());
        } catch (ParsingException exception) {
            ServletUtils.writeServiceResponse(
                    response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException(exception.getMessage())
                    ), null
            );
            return;
        }

        Run run = RunToRestRunDtoConversor.toRun(runDto);

        try {
            run = RunServiceFactory.getService().addRun(run);
        } catch (InputValidationException exception) {
            ServletUtils.writeServiceResponse(
                    response, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(exception), null
            );
            return;
        }

        runDto = RunToRestRunDtoConversor.toRestRunDto(run);

        String runHeaders = ServletUtils.normalizePath(request.getRequestURL().toString()) + "/" + run.getRunId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", runHeaders);

        ServletUtils.writeServiceResponse(
                response, HttpServletResponse.SC_CREATED,
                JsonToRestRunDtoConversor.toObjectNode(runDto), headers
        );

    }

}
