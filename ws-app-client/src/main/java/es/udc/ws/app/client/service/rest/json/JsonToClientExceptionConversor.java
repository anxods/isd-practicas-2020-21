package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream e) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(e);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON");
            } else {
                String errorType = rootNode.get("errorType").textValue();

                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else if (errorType.equals("RunCelebration")) {
                    return toRunCelebrationException(rootNode);
                } else if (errorType.equals("DorsalAlreadyTaken")) {
                    return toDorsalAlreadyTakenException(rootNode);
                } else if (errorType.equals("MaxParticipants")) {
                    return toMaxParticipantsException(rootNode);
                } else if (errorType.equals("LateInscription")) {
                    return toLateInscriptionException(rootNode);
                } else if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                }
                else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }

        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ParsingException(ex);
        }

    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromGoneErrorCode(InputStream e) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(e);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON");
            }
            else {
                String errorType = rootNode.get("errorType").textValue();

                if (errorType.equals("RunCelebration")) {
                    return toRunCelebrationException(rootNode);
                }
                else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ParsingException(ex);
        }
    }

    public static Exception fromNotFoundErrorCode(InputStream e) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(e);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON");
            }
            else {
                String errorType = rootNode.get("errorType").textValue();

                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else if (errorType.equals("InscriptionNotFound")) {
                    return toInscriptionNotFoundException(rootNode);
                } else if (errorType.equals("IncorrectCreditCard")) {
                    return toIncorrectCreditCardException(rootNode);
                }
                else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ParsingException(ex);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    private static ClientMaxParticipantsException toMaxParticipantsException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientMaxParticipantsException(message);
    }

    private static ClientNoParticipantsException toNoParticipantsException(JsonNode rootNode) {
        Long runId = rootNode.get("runId").longValue();
        int runParticipants = rootNode.get("runParticipants").intValue();
        return new ClientNoParticipantsException(runId, runParticipants);
    }

    private static ClientDorsalAlreadyTakenException toDorsalAlreadyTakenException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientDorsalAlreadyTakenException(message);
    }

    private static ClientInscriptionNotFoundException toInscriptionNotFoundException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientInscriptionNotFoundException(message);
    }

    private static ClientLateInscriptionException toLateInscriptionException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientLateInscriptionException(message);
    }

    private static ClientRunCelebrationException toRunCelebrationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientRunCelebrationException(message);
    }

    private static ClientIncorrectCreditCardException toIncorrectCreditCardException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new ClientIncorrectCreditCardException(message);
    }

}
