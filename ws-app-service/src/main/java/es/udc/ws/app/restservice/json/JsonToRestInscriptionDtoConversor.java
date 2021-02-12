package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestInscriptionDtoConversor {

    public static ObjectNode toObjectNode(RestInscriptionDto inscription) {

        ObjectNode inscriptionNode = JsonNodeFactory.instance.objectNode();

        if (inscription.getInscriptionId() != null) {
            inscriptionNode.put("inscriptionId", inscription.getInscriptionId());
        }

        inscriptionNode.put("runId", inscription.getRunId())
                .put("inscriptionDorsal", inscription.getInscriptionDorsal())
                .put("inscriptionUserEmail", inscription.getInscriptionUserEmail())
                .put("inscriptionCreditCardNumber", inscription.getInscriptionCreditCardNumber());

        return inscriptionNode;
    }

    public static ArrayNode toArrayNode(List<RestInscriptionDto> inscriptionDtos) {

        ArrayNode inscriptionsNode = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < inscriptionDtos.size(); i++) {
            RestInscriptionDto inscriptionDto = inscriptionDtos.get(i);
            ObjectNode inscriptionObject = toObjectNode(inscriptionDto);
            inscriptionsNode.add(inscriptionObject);
        }

        return inscriptionsNode;

    }

    public static RestInscriptionDto toServiceInscriptionDto(InputStream jsonInscription) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscription);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON");
            } else {
                ObjectNode inscriptionObject = (ObjectNode) rootNode;

                JsonNode inscriptionIdNode = inscriptionObject.get("inscriptionId");
                Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;

                Long runId = inscriptionObject.get("runId").longValue();
                String inscriptionDorsal = inscriptionObject.get("inscriptionDorsal").textValue().trim();
                String inscriptionUserEmail = inscriptionObject.get("inscriptionUserEmail").textValue().trim();
                String inscriptionCreditCardNumber = inscriptionObject.get("inscriptionCreditCardNumber").textValue().trim();

                return new RestInscriptionDto(inscriptionId, runId, inscriptionDorsal, inscriptionUserEmail, inscriptionCreditCardNumber);
            }

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

}
