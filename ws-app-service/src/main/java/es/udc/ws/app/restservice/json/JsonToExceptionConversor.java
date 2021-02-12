package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.Run.exceptions.*;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonToExceptionConversor {

    public static ObjectNode toInputValidationException(InputValidationException exception) {

        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "InputValidation");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException exception) {

        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();
        ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "InstanceNotFound");
        exceptionObjectNode.put("instanceId", (exception.getInstanceId() != null) ?
                exception.getInstanceId().toString() : null
        );
        exceptionObjectNode.put("instanceType",
                exception.getInstanceType().substring(
                        exception.getInstanceType().lastIndexOf('.') + 1
                )
        );

        return exceptionObjectNode;
    }

    public static ObjectNode toRunCelebrationException (RunCelebrationException exception) {
        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "RunCelebration");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toDorsalAlreadyTakenException(DorsalAlreadyTakenException exception) {
        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "DorsalAlreadyTaken");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toIncorrectCreditCardException(IncorrectCreditCardException exception) {
        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "IncorrectCreditCard");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toInscriptionNotFoundException (InscriptionNotFoundException exception) {
        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "InscriptionNotFound");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toLateInscriptionException (LateInscriptionException exception) {
        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "LateInscription");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toMaxParticipantsException(MaxParticipantsException exception) {

        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "MaxParticipants");
        exceptionObjectNode.put("message", exception.getMessage());

        return exceptionObjectNode;
    }

    public static ObjectNode toNoParticipantsException(NoParticipantsException exception) {

        ObjectNode exceptionObjectNode = JsonNodeFactory.instance.objectNode();
        ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();

        exceptionObjectNode.put("errorType", "NoParticipants");
        exceptionObjectNode.put("runId", (exception.getRunId() != null) ?
                exception.getRunId().toString() : null
        );
        exceptionObjectNode.put("runParticipants",
                (Integer.toString(exception.getRunParticipants()) != null) ?
                        Integer.toString(exception.getRunParticipants()) : null
        );

        return exceptionObjectNode;
    }

}
