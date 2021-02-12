package es.udc.ws.app.model.runservice.exceptions;

import java.lang.String;

@SuppressWarnings("serial")
public class LateInscriptionException extends Exception {
    public LateInscriptionException (String errorMessage) {
        super(errorMessage);
    }

    public LateInscriptionException() {
        super("Run already occured, cannot register...");
    }
}
