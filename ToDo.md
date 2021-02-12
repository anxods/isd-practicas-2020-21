## Cosas por hacer 

### General

- [x] El interfaz del servicio del modelo sólo debería tener 6 métodos.
- [x] Añadir excepciones propias en JsonToExceptionConversor de service.
- [x] Completar el package service-exceptions de client.
- [x] Completar el package service-json de client.
     - [x] Completar JsonToClientRunDtoConversor
     - [x] Completar JsonToClientInscriptionDtoConversor
     - [x] Completar JsonToClientExceptionDtoConversor
- [x] Completar el package service-thrift de client.

### Funcionalidades 1 y 3 (Javi)


### Funcionalidades 2 y 6 (Adri)


### Funcionalidades 4 y 5 (Anxo)

- [x] Revisar la actualización de participantes de una carrera.

```java
run.addParticipant(); 

runDao.update(connection, run);

/* Commit. */
connection.commit();

return inscription.getInscriptionId();
```

- [x] Revisar que la transaccionalidad se gestiona de forma adecuada.
- [x] Revisar validaciones básicas al crear inscripciones.

```java
// A la validación ya existente de la tarjeta de crédito... 
PropertyValidator.validateCreditCard(inscriptionCreditCardNumber);

// ... Le añadimos la del mail del user con las siguientes funciones:
private boolean isMailValid(String email) {
 
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
         "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
    
    return pat.matcher(email).matches();

    
}

private void validateMail(String mail) throws InputValidationException {

    PropertyValidator.validateMandatoryString("inscriptionUserEmail", mail);

     if (!isMailValid(mail)) {
         throw new InputValidationException("Invalid mail: " + mail);
     }

}

// Luego en registerInRun usamos la función antes de hacer nada:
validateMail(inscriptionUserEmail);

// Y finalmente en RunServiceTest.java añadimos un test para comprobar
// el funcionamiento:
@Test
public void testRegisterWithInvalidMail() throws
        InstanceNotFoundException, InputValidationException, LateInscriptionException, MaxParticipantsException{
 
        Run run = createRun(getValidRun());
        try {
            assertThrows(InputValidationException.class, () -{
                Long code = runService.registerInRun(run.getRunId(), INVALID_MAIL, VALID_CREDIT_CARD_NUMBER);
                removeInscription(code);
            });
        } finally {
            // Clear database
            removeRun(run.getRunId());
        }
 
}
```

 - [x] Revisar la generación de número de dorsal.

``` java
// Como vemos en la generación de la inscripción, el dorsal será un String
// que se formará a partir del número de participantes actuales + 1, de manera
// que siempre se asignarán distintos dorsales a cada participante. 
Inscription inscription = inscriptionDao.create(
        connection, new Inscription(runId, Integer.toString(run.getRunParticipants() + 1),
                inscriptionUserEmail, inscriptionCreditCardNumber, LocalDateTime.now(),
                false
        )
);
```

- [x] Añadir en el RunServiceClient de client funcionalidades 4 y 5.