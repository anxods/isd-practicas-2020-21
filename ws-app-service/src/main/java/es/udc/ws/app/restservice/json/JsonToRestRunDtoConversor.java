package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestRunDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestRunDtoConversor {

    public static ObjectNode toObjectNode(RestRunDto run) {

        ObjectNode runObject = JsonNodeFactory.instance.objectNode();

        if (run.getRunId() != null) {
            runObject.put("runId", run.getRunId());
        }

        runObject.put("runLocation", run.getRunLocation())
                .put("runDescription", run.getRunDescription())
                .put("runStartDate", run.getRunStartDate())
                .put("runPrice", run.getRunPrice())
                .put("runMaxParticipants", run.getRunMaxParticipants())
                .put("runParticipants", run.getRunParticipants());

        return runObject;
    }

    public static ArrayNode toArrayNode(List<RestRunDto> runs) {

        ArrayNode runsNode = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < runs.size(); i++) {
            RestRunDto runDto = runs.get(i);
            ObjectNode runObject = toObjectNode(runDto);
            runsNode.add(runObject);
        }

        return runsNode;
    }

    public static RestRunDto toServiceRunDto(InputStream jsonRun)
        throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRun);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode runObject = (ObjectNode) rootNode;

                JsonNode runIdNode = runObject.get("runId");
                Long runId = (runIdNode != null) ? runIdNode.longValue() : null;

                String runLocation = runObject.get("runLocation").textValue().trim();
                String runDescription = runObject.get("runDescription").textValue().trim();
                String runStartDate = runObject.get("runStartDate").textValue().trim();
                float runPrice = runObject.get("runPrice").floatValue();
                int runMaxParticipants = runObject.get("runMaxParticipants").intValue();
                int runParticipants = runObject.get("runParticipants").intValue();

                return new RestRunDto(runId, runLocation, runDescription, runStartDate,
                        runPrice, runMaxParticipants, runParticipants);
            }
        } catch (ParsingException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ParsingException(exception);
        }
    }

}
