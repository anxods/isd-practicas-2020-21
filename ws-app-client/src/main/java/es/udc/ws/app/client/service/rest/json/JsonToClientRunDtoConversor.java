package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientRunDto;

import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

public class JsonToClientRunDtoConversor {

    public static ObjectNode toObjectNode(ClientRunDto runDto) throws IOException {

        ObjectNode runObject = JsonNodeFactory.instance.objectNode();

        if (runDto.getRunId() != null) {
            runObject.put("runId", runDto.getRunId());
        }

        runObject.put("runLocation", runDto.getRunLocation()).
                put("runDescription", runDto.getRunDescription()).
                put("runStartDate", runDto.getRunStartDate()).
                put("runPrice", runDto.getRunPrice()).
                put("runMaxParticipants", runDto.getRunMaxParticipants()).
                put("runParticipants", runDto.getRunParticipants());

        return runObject;

    }

    private static ClientRunDto toClientRunDto(JsonNode jsonNode) throws ParsingException {

        if (jsonNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON");
        } else {
            ObjectNode runObject = (ObjectNode) jsonNode;

            JsonNode runIdNode = runObject.get("runId");
            Long runId = (runIdNode != null) ? runIdNode.longValue() : null;

            String runLocation = runObject.get("runLocation").textValue().trim();
            String runDescription = runObject.get("runDescription").textValue().trim();
            String runStartDate = runObject.get("runStartDate").textValue().trim();
            float runPrice = runObject.get("runPrice").floatValue();
            int runMaxParticipants = runObject.get("runMaxParticipants").intValue();
            int runParticipants = runObject.get("runParticipants").intValue();

            return new ClientRunDto(runId, runLocation, runDescription, runStartDate, runPrice, runMaxParticipants, runParticipants);
        }

    }

    public static ClientRunDto toClientRunDto(InputStream inputStream) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(inputStream);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON");
            } else {
                return toClientRunDto(rootNode);
            }

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

    public static List<ClientRunDto> toClientRunDtos(InputStream jsonRuns) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRuns);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON");
            } else {
                ArrayNode runsArray = (ArrayNode) rootNode;
                List<ClientRunDto> runDtos = new ArrayList<>(runsArray.size());

                for (JsonNode runNode : runsArray) {
                    runDtos.add(toClientRunDto(runNode));
                }

                return runDtos;

            }

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

}
