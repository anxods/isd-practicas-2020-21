package es.udc.ws.app.model.runservice;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.model.Inscription.SqlInscriptionDaoFactory;
import es.udc.ws.app.model.Inscription.SqlInscriptionDao;
import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Run.SqlRunDao;
import es.udc.ws.app.model.Run.SqlRunDaoFactory;
import static es.udc.ws.app.model.util.ModelConstants.*;
import java.sql.*;
import java.time.LocalDateTime;


import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;
import java.sql.SQLException;

import java.util.List;
import java.util.regex.Pattern;


public class RunServiceImpl implements RunService {
    /*
     * IMPORTANT: Some JDBC drivers require "setTransactionIsolation" to be called
     * before "setAutoCommit".
     */

    private final DataSource dataSource;
    private SqlInscriptionDao inscriptionDao = null;
    private SqlRunDao runDao = null;

    public RunServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(RUN_DATA_SOURCE);
        inscriptionDao = SqlInscriptionDaoFactory.getDao();
        runDao = SqlRunDaoFactory.getDao();
    }

    private void validateRun(Run run) throws InputValidationException {

        PropertyValidator.validateMandatoryString("runLocation", run.getRunLocation());
        PropertyValidator.validateMandatoryString("runDescription", run.getRunDescription());
        PropertyValidator.validateDouble("runPrice", run.getRunPrice(),0, MAX_PRICE);
        PropertyValidator.validateLong("runMaxParticipants", run.getRunMaxParticipants(),0, run.getRunMaxParticipants());
        PropertyValidator.validateLong("runParticipants",run.getRunParticipants(),0, run.getRunMaxParticipants());

    }

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

    /**
     * Funcionalidad #1 Añadir una carrera
     * @param run
     * @return Objeto tipo Run
     * @throws InputValidationException
     */
    @Override
    public Run addRun(Run run) throws InputValidationException {

        validateRun(run);
        run.setRunCreationDate(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Run createdRun= runDao.create(connection, run);

                /* Commit. */
                connection.commit();

                return createdRun;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**Funcionalidad #2 - Buscar carreras.
     * Será posible buscar carreras por su identificador.
     *
     * @return Run object
     * @throws InstanceNotFoundException tag If the run does not exist
     * @throws RunCelebrationException tag If the run has already taken place.
     * .*/
    @Override
    public Run findRun(Long runId) throws InstanceNotFoundException, RunCelebrationException {

        try (Connection connection = dataSource.getConnection()) {

            Run run = runDao.find(connection, runId);
            LocalDateTime now = LocalDateTime.now();

            // Check if the Run has already taken place
            if (run.getRunStartDate().isAfter(now)) {
                return run;
            } else {            // if it has taken place, throw exception
                throw new RunCelebrationException(run.getRunId(), run.getRunStartDate());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funcionalidad #3 Buscar carreras en función de dos parámetros dados:
     * la ciudad y la fecha de estas.
     * @param city
     * @param date
     * @return Lista de objetos tipo Run
     */
    @Override
    public List<Run> findRuns(String city, String date) {

        String keywords = city+" "+date;

        try (Connection connection = dataSource.getConnection()) {
            return runDao.findByKeywords(connection, city, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Funcionalidad #4 - Permitir la inscripción de un usuario en una carrera hasta 24 horas antes
     * de su inicio.
     * @param runId
     * @param inscriptionUserEmail
     * @param inscriptionCreditCardNumber
     * @return En caso de ejecutarse con éxito, devuelve un código que será necesario para recoger el
     * dorsal, y se almacena la inscripción, quedando registrado el número de dorsal asignado al
     * participante y la fecha y hora a la que se hizo la inscripción
     * @throws InstanceNotFoundException
     * @throws InputValidationException
     * @throws LateInscriptionException
     */
    @Override
    public Long registerInRun(Long runId, String inscriptionUserEmail,
                                     String inscriptionCreditCardNumber)
            throws InstanceNotFoundException, InputValidationException, LateInscriptionException,
                MaxParticipantsException {

        validateMail(inscriptionUserEmail);
        PropertyValidator.validateCreditCard(inscriptionCreditCardNumber);

        try (Connection connection = dataSource.getConnection()) {
            try {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Run run = runDao.find(connection, runId);

                LocalDateTime runDay = run.getRunStartDate();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime runDayMinusOne = runDay.minusDays(1);

                try {
                    if (now.isBefore(runDayMinusOne)) {
                        Inscription inscription = inscriptionDao.create(
                                connection, new Inscription(runId, Integer.toString(run.getRunParticipants() + 1),
                                        inscriptionUserEmail, inscriptionCreditCardNumber, LocalDateTime.now(),
                                        false
                                )
                        );

                        // Add a participant to the run
                        run.addParticipant();

                        runDao.update(connection, run);

                        /* Commit. */
                        connection.commit();

                        return inscription.getInscriptionId();
                    } else {
                        throw new LateInscriptionException("LateInscriptionException: Registered too late. ");
                    }
                } catch (LateInscriptionException e) {
                    throw new LateInscriptionException("LateInscriptionException: Registered too late. " + e);
                } catch (MaxParticipantsException e) {
                    throw new MaxParticipantsException(run.getRunId());
                }

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funcionalidad #5 - Buscar inscripciones dadas determinadas condiciones
     * (número de tarjeta, mail del corredor...)
     * @param mail
     * @return Lista de objetos Inscription que coinciden con las condiciones dadas.
     */
    @Override
    public List<Inscription> findInscriptions(String mail) throws InputValidationException {
        validateMail(mail);
        try (Connection connection = dataSource.getConnection()) {
            return inscriptionDao.findByEmail(connection, mail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funcionalidad #6 - Recogida de dorsal.
     * Hace una serie de comprobaciones para saber si se puede o no recoger el dorsal. En caso de que sí,
     * se considera como dorsal recogido.
     * @return boolean: True -> se acaba de recoger ; False -> no se pudo recoger.
     * @throws RuntimeException tag The SQL fails at runtime.
     * @throws InscriptionNotFoundException tag The inscription does not exist.
     * @throws RunCelebrationException tag If the run has already taken place.
     * @throws DorsalAlreadyTakenException tag The dorsal has been already taken.
     * @throws IncorrectCreditCardException tag The Credit card does not correspond.
     */
    @Override
    public Inscription takeDorsal(Long inscriptionId, String inscriptionCreditCardNumber)
            throws RuntimeException, InscriptionNotFoundException, RunCelebrationException,
            DorsalAlreadyTakenException, IncorrectCreditCardException {

        try (Connection connection = dataSource.getConnection()) {

            // Checks if the inscriptionId is correct
            // @throws InscriptionNotFoundException
            Inscription inscription = inscriptionDao.find(connection, inscriptionId);

            // Check if the Run has not taken place yet
            // @throws RunCelebrationException
            findRun(inscription.getRunId());


            // Check if the Credit Card number is correct
            // @throws IncorrectCreditCardException
            if (!inscription.getInscriptionCreditCardNumber().equals(inscriptionCreditCardNumber)){
                // Throws an exception for the CreditCard.
                throw new IncorrectCreditCardException(inscriptionId, inscriptionCreditCardNumber);
            }

            // Check if the dorsal has not already been taken
            // @throws DorsalAlreadyTakenException
            if (inscription.isDorsalTaken()){
                // Throws an exception because the dorsal has already been taken
                throw new DorsalAlreadyTakenException(inscriptionId);

            }

            // We change the isDorsalTaken to true (we are giving it now)
            inscription.setIsDorsalTaken(true);

            // Tries to update the database
            inscriptionDao.update(connection, inscription);

            // The dorsal has been correctly given
            return inscription;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstanceNotFoundException e) {
            // The inscription identifier doesn't exist
            throw new InscriptionNotFoundException(inscriptionId);
        } catch(RunCelebrationException e) {
            // The competition has already taken place
            throw new RunCelebrationException(inscriptionId);
        }
    }

}
