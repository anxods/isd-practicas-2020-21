namespace java es.udc.ws.app.thrift

struct ThriftRunDto {
    1: i64 runId;
    2: string runLocation;
    3: string runDescription;
    4: string runStartDate;
    5: double runPrice;
    6: i32 runMaxParticipants;
    7: i32 runParticipants;
}

struct ThriftInscriptionDto {
    1: i64 inscriptionId;
    2: i64 runId;
    3: string inscriptionDorsal;
    4: string inscriptionUserEmail;
    5: string inscriptionCreditCardNumber;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftRuntimeException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftDorsalAlreadyTakenException {
    1: string message
}

exception ThriftIncorrectCreditCardException {
    1: string message
}

exception ThriftInscriptionNotFoundException {
    1: string message
}

exception ThriftLateInscriptionException {
    1: string message
}

exception ThriftRunCelebrationException {
    1: string message
}

exception ThriftMaxParticipantsException {
    1: i64 runId
}

exception ThriftNoParticipantsException {
    1: i64 runId
    2: i32 runParticipants
}

service ThriftRunService {
    // Funcionalidad #1: Run addRun(Run run)
    ThriftRunDto addRun(1: ThriftRunDto runDto) throws (1: ThriftInputValidationException e)

    // Funcionalidad #2: Run findRun(Long runId)
    ThriftRunDto findRun(1: i64 runId) throws (1: ThriftInstanceNotFoundException e)

    // Funcionalidad #3: List<Run> findRuns(String city, String date)
    list<ThriftRunDto> findRuns(1: string city, 2: string date);

    // Funcionalidad #4: Long registerInRun(Long runId, String inscriptionUserEmail, String inscriptionCreditCardNumber)
    i64 registerInRun(1: i64 runId, 2: string inscriptionUserEmail, 3: string inscriptionCreditCardNumber) throws (1: ThriftInstanceNotFoundException e, 2:ThriftInputValidationException ee, 3: ThriftLateInscriptionException eee, 4: ThriftMaxParticipantsException eeee)

    // Funcionalidad #5: List<Inscription> findInscriptions(String keywords)
    list<ThriftInscriptionDto> findInscriptions(1: string keywords) throws (1: ThriftInputValidationException e);

    // Funcionalidad #6: boolean takeDorsal(Long inscriptionId, String inscriptionCreditCardNumber)
    //                             throws RuntimeException, InscriptionNotFoundException, RunCelebrationException,
    //                                    DorsalAlreadyTakenException, IncorrectCreditCardException
    ThriftInscriptionDto takeDorsal(1: i64 inscriptionId, 2: string inscriptionCreditCardNumber) throws (1: ThriftRuntimeException e, 2: ThriftInscriptionNotFoundException ee, 3: ThriftRunCelebrationException eee, 4: ThriftDorsalAlreadyTakenException eeee, 5: ThriftIncorrectCreditCardException eeeee)
}