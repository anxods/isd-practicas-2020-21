package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientInscriptionDto;

import es.udc.ws.app.client.service.dto.ClientRunDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientInscriptionDtoConversor {

    public static ObjectNode toObjectNode(ClientInscriptionDto inscriptionDto) throws IOException {

        ObjectNode inscriptionObject = JsonNodeFactory.instance.objectNode();

        if (inscriptionDto.getInscriptionId() != null) {
            inscriptionObject.put("inscriptionId", inscriptionDto.getInscriptionId());
        }

        inscriptionObject.put("runId", inscriptionDto.getRunId())
                .put("inscriptionDorsal", inscriptionDto.getInscriptionDorsal())
                .put("inscriptionUserEmail", inscriptionDto.getInscriptionUserEmail());

        return inscriptionObject;

    }

    public static ClientInscriptionDto toClientInscriptionDto(InputStream inputStream) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(inputStream);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON");
            } else {
                return toClientInscriptionDto(rootNode);
            }

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

    private static ClientInscriptionDto toClientInscriptionDto(JsonNode inscriptionNode) throws ParsingException {

        if (inscriptionNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON");
        } else {
            ObjectNode inscriptionObject = (ObjectNode) inscriptionNode;

            JsonNode inscriptionIdNode = inscriptionObject.get("inscriptionId");
            Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;

            Long runId = inscriptionObject.get("runId").longValue();
            String inscriptionDorsal = inscriptionObject.get("inscriptionDorsal").textValue().trim();
            String inscriptionUserEmail = inscriptionObject.get("inscriptionUserEmail").textValue().trim();
            String inscriptionCreditCardNumber = inscriptionObject.get("inscriptionCreditCardNumber").textValue().trim();

            return new ClientInscriptionDto(inscriptionId, runId, inscriptionDorsal, inscriptionUserEmail, inscriptionCreditCardNumber);
        }

    }

    public static List<ClientInscriptionDto> toClientInscriptionDtos(InputStream jsonInscriptions) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscriptions);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON");
            } else {
                ArrayNode inscriptionsArray = (ArrayNode) rootNode;
                List<ClientInscriptionDto> inscriptionDtos = new ArrayList<>(inscriptionsArray.size());

                for (JsonNode inscriptionNode : inscriptionsArray) {
                    inscriptionDtos.add(toClientInscriptionDto(inscriptionNode));
                }

                return inscriptionDtos;

            }

        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

}
