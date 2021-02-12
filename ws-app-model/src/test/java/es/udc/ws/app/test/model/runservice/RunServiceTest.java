package es.udc.ws.app.test.model.runservice;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.app.model.Inscription.SqlInscriptionDao;
import es.udc.ws.app.model.Inscription.SqlInscriptionDaoFactory;
import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Run.SqlRunDao;
import es.udc.ws.app.model.Run.SqlRunDaoFactory;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.RunService;
import es.udc.ws.app.model.runservice.RunServiceFactory;
import es.udc.ws.app.model.runservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class RunServiceTest {

    private final long NON_EXISTENT_RUN_ID = -1;
    private final long NON_EXISTENT_INSCRIPTION_ID = -1;
    private final String USER_ID = "ws-user";

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String VALID_CREDIT_CARD_NUMBER_2 = "1234567890123451";
    private final String INVALID_CREDIT_CARD_NUMBER = "";

    private final String VALID_MAIL = "run@isd.com";
    private final String VALID_MAIL_2 = "run2@isd.com";
    private final String INVALID_MAIL = "run";

    private static RunService runService = null;

    private static SqlInscriptionDao inscriptionDao = null;

    private static SqlRunDao runDao = null;

    private static DataSource dataSource = null;

    @BeforeAll
    public static void init() {

        dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(RUN_DATA_SOURCE, dataSource);

        runService = RunServiceFactory.getService();

        inscriptionDao = SqlInscriptionDaoFactory.getDao();

        runDao = SqlRunDaoFactory.getDao();

    }

    public Inscription findInscriptionS(Long inscriptionId, String inscriptionCreditCardNumber)
            throws RuntimeException, InscriptionNotFoundException {

        try (Connection connection = dataSource.getConnection()) {

            // Checks if the inscriptionId is correct
            // @throws InscriptionNotFoundException

            return inscriptionDao.find(connection, inscriptionId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstanceNotFoundException e) {
            // The inscription identifier doesn't exist
            throw new InscriptionNotFoundException("The inscription with ID = " + inscriptionId + " does not exist",
                    e);
        }
    }

    public void removeRunS(Long runId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                runDao.remove(connection, runId);

                /* Commit. */
                connection.commit();

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

    public void updateRunS(Run run) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                runDao.update(connection, run);

                /* Commit. */
                connection.commit();

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

    private Run createRun(Run run) {

        Run addedRun = null;
        try {
            addedRun = runService.addRun(run);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedRun;

    }

    private void updateRun(Run run) throws InstanceNotFoundException {
        updateRunS(run);
    }

    private void removeRun(Long runId) {

        try {
            removeRunS(runId);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void removeInscription(Long inscriptionId) {

        DataSource dataSource = DataSourceLocator.getDataSource(RUN_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                inscriptionDao.remove(connection, inscriptionId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Run getValidRun() {
        return new Run("Location", "Description", LocalDateTime.of(2022,1,30,10,00),
                (float) 20, 1000, 0);
    }

    private Run getInValidRun() {
        return new Run("Run Location", "Run description", LocalDateTime.now().minusMinutes(1), (float) 20, 1000, 0);
    }

    private Run getFullRun() {
        return new Run("Location", "Description", LocalDateTime.of(2022,1,30,10,00),
                (float) 20, 1000, 1000);
    }

    @Test
    public void testAddRun() throws
            InputValidationException, InstanceNotFoundException, RunCelebrationException{

        Run run = createRun(getValidRun());

        try{
            Run foundRun = runService.findRun(run.getRunId());
            assertEquals(run, foundRun);

        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test
    public void testFindRuns() {

        List<Run> runs = new ArrayList<Run>();

        Run run = createRun(getValidRun());

        runs.add(run);

        try{
            List<Run> foundRuns = runService.findRuns(run.getRunLocation(),run.getRunStartDate().toString());
            assertEquals(runs, foundRuns);
        }finally {
            removeRun(run.getRunId());
        }
    }


    @Test
    public void testRegisterInRun() throws
            InstanceNotFoundException, InputValidationException, LateInscriptionException, InscriptionNotFoundException, MaxParticipantsException {
        Run run = createRun(getValidRun()); // Run that occurs in a few months

        try{
            Long code = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
            Inscription inscription = findInscriptionS(code, VALID_CREDIT_CARD_NUMBER);
            assertEquals(code, inscription.getInscriptionId());
        } finally {
            removeRun(run.getRunId());
        }
    }


    @Test
    public void testInvalidRegisterInRun() throws
            InstanceNotFoundException, InputValidationException, LateInscriptionException, MaxParticipantsException {

        Run run = createRun(getInValidRun()); // Run that occurs now

        try {
            assertThrows(LateInscriptionException.class, () -> runService.registerInRun(run.getRunId(),VALID_MAIL,VALID_CREDIT_CARD_NUMBER));
        } finally {
            removeRun(run.getRunId());
        }

    }

    @Test
    public void testRegisterInNonExistentRun() throws
            InstanceNotFoundException, InputValidationException, LateInscriptionException, MaxParticipantsException {
        assertThrows(InstanceNotFoundException.class, () -> {
            Long code = runService.registerInRun(NON_EXISTENT_RUN_ID, VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
        });
    }

    @Test
    public void testRegisterWithInvalidMail() throws
            InstanceNotFoundException, InputValidationException, LateInscriptionException, MaxParticipantsException{

        Run run = createRun(getValidRun());
        try {
            assertThrows(InputValidationException.class, () -> {
                Long code = runService.registerInRun(run.getRunId(), INVALID_MAIL, VALID_CREDIT_CARD_NUMBER);
                removeInscription(code);
            });
        } finally {
            // Clear database
            removeRun(run.getRunId());
        }

    }

    @Test
    public void testRegisterWithInvalidCard() throws
            InstanceNotFoundException, InputValidationException, LateInscriptionException, MaxParticipantsException{

        Run run = createRun(getValidRun());
        try {
            assertThrows(InputValidationException.class, () -> {
                Long code = runService.registerInRun(run.getRunId(), VALID_MAIL, INVALID_CREDIT_CARD_NUMBER);
                removeInscription(code);
            });
        } finally {
            // Clear database
            removeRun(run.getRunId());
        }

    }

    @Test
    public void testRegisterInFullRun() throws
            InstanceNotFoundException, InputValidationException, LateInscriptionException,
            MaxParticipantsException {

        Run run = createRun(getFullRun());
        try {
            assertThrows(MaxParticipantsException.class, () -> {
                Long code = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
                removeInscription(code);
            });
        } finally {
            removeRun(run.getRunId());
        }

    }

    @Test
    public void testRegistercheckParticipants() throws InstanceNotFoundException, InputValidationException, LateInscriptionException,
            MaxParticipantsException, RunCelebrationException {

        Run run = createRun(getValidRun());

        try {
            Long code = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
            Run run_after = runService.findRun(run.getRunId());
            assertEquals(1, run_after.getRunParticipants());
            removeInscription(code);
        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test
    public void testFindNonExistentInscription() {
        assertThrows(InscriptionNotFoundException.class, () -> findInscriptionS(NON_EXISTENT_INSCRIPTION_ID, VALID_CREDIT_CARD_NUMBER));
    }

    @Test
    public void testFindInscriptions() throws InputValidationException, InstanceNotFoundException, LateInscriptionException, InscriptionNotFoundException, MaxParticipantsException {

        // First we create the list
        List<Inscription> inscriptions = new LinkedList<Inscription>();

        // Then we create a Run and Inscriptions and add them to it
        Run run = createRun(getValidRun());
        Long inscriptionCode1 = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
        Inscription inscription1 = findInscriptionS(inscriptionCode1, VALID_MAIL_2);
        inscriptions.add(inscription1);
        Long inscriptionCode2 = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
        Inscription inscription2 = findInscriptionS(inscriptionCode2, VALID_MAIL_2);
        inscriptions.add(inscription2);

        try {
            List<Inscription> foundInscriptions = runService.findInscriptions(VALID_MAIL);
            assertEquals(inscriptions, foundInscriptions);

            assertEquals(2, foundInscriptions.size());
        } finally {
            removeRun(run.getRunId());
        }

    }

    @Test
    public void testFindInscriptionsDifferentLists() throws
            InputValidationException, InstanceNotFoundException, LateInscriptionException, InscriptionNotFoundException, MaxParticipantsException {

        // First we create the list
        List<Inscription> inscriptions = new LinkedList<Inscription>();

        // Then we create a Run and Inscriptions and add them to it
        // This time we use a different credit card, so that the lists do not match
        Run run = createRun(getValidRun());
        Long inscriptionCode1 = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER_2);
        Inscription inscription1 = findInscriptionS(inscriptionCode1, VALID_MAIL_2);
        inscriptions.add(inscription1);
        Long inscriptionCode2 = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER_2);
        Inscription inscription2 = findInscriptionS(inscriptionCode2, VALID_CREDIT_CARD_NUMBER);
        inscriptions.add(inscription2);

        try {
            List<Inscription> foundInscriptions = runService.findInscriptions(VALID_MAIL);
            assertNotSame(inscriptions, foundInscriptions);
        } finally {
            removeRun(run.getRunId());
        }

    }

    /**
     * Tests para el caso de uso #2  -- findRun
     */


    @Test   // The Run is not in the DB
    public void testFindRunInvalidId() {

        assertThrows(InstanceNotFoundException.class, () ->
                runService.findRun(NON_EXISTENT_RUN_ID));
    }

    @Test   // The Run has already taken place
    public void testFindRunInvalidRun() {
        // We create a run that has already taken place.
        Run run = createRun(getInValidRun());

        // We try to search for it, it has to return a RunCelebrationException
        try{
            assertThrows(RunCelebrationException.class, () -> {
                Run run2 = runService.findRun(run.getRunId());
            });
        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test   // The Run is correct. Expected behaviour
    public void testFindRunValidRun() {
        // We create a valid run
        Run validRun = createRun(getValidRun());
        try {
            Run runAux = runService.findRun(validRun.getRunId());
            assertTrue(validRun.equals(runAux));
        } catch (RunCelebrationException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Deleting the Run from the DB
            removeRun(validRun.getRunId());
        }
    }

    @Test   // Correct Run, modificated, check both equals
    public void testFindRunValidModified() {
        // Creation of the Run
        Run validRun = createRun(getValidRun());
        try {
            Run runAux = runService.findRun(validRun.getRunId());
            assertTrue(validRun.equals(runAux));

            // Modified # of participants
            for (int i = 0; i < 100; i++)
                validRun.addParticipant();
            // Run updated in the DB
            updateRun(validRun);
            assertFalse(validRun.equals(runAux));

            // updating runAux...
            runAux = runService.findRun(validRun.getRunId());

            // Check they both are equals
            assertTrue(validRun.equals(runAux));

        } catch (MaxParticipantsException e) {
            e.printStackTrace();
        } catch (RunCelebrationException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } finally {
            removeRun(validRun.getRunId());
        }
    }

    @Test   // The Run has been deleted from the DB
    public void testFindRunDeletedRun() {
        // Creation of the Run
        Run validRun = createRun(getValidRun());
        try {
            Run runAux = runService.findRun(validRun.getRunId());
            assertTrue(validRun.equals(runAux));

            // Deletion of the run
            removeRun(validRun.getRunId());

            // Checks that it's not in the DB
            assertThrows(InstanceNotFoundException.class, () -> runService.findRun(runAux.getRunId()));

        } catch (RunCelebrationException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Tests para el caso de uso #6  --  takeDorsal
     */

    @Test   // Test if the Inscription ID is not correct
    public void testTakeDorsalInscriptionIdNotCorrect() {
        Run run = createRun(getValidRun());
        try {
            // The Inscription must be not found, so it throws an exception
            assertThrows(InscriptionNotFoundException.class, () -> runService.takeDorsal(NON_EXISTENT_INSCRIPTION_ID, VALID_CREDIT_CARD_NUMBER));
        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test   // Test if the CreditCardNumber is not correct
    public void testTakeDorsalInvalidCreditCard() {
        Run run = createRun(getValidRun());

        try {
            Long inscriptionId = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);
            assertThrows(IncorrectCreditCardException.class, () -> {
                runService.takeDorsal(inscriptionId, VALID_CREDIT_CARD_NUMBER_2);
            });
        } catch (LateInscriptionException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (InputValidationException e) {
            e.printStackTrace();
        } catch (MaxParticipantsException e) {
            e.printStackTrace();
        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test   // Test if the Run has already taken place
    public void testTakeDorsalRunAlreadyRun() {
        Run run = createRun(getInValidRun());
        try {
            Long inscriptionId = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);

            // Try to get the dorsal
            assertThrows(RunCelebrationException.class, () -> {
                runService.takeDorsal(inscriptionId, VALID_CREDIT_CARD_NUMBER);
            });

        } catch (LateInscriptionException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (InputValidationException e) {
            e.printStackTrace();
        } catch (MaxParticipantsException e) {
            e.printStackTrace();
        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test   // Test if everything goes fine
    public void testTakeDorsalOK() {
        Run run = createRun(getValidRun());
        try {
            Long inscriptionId = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);

            // Everything must be fine
            runService.takeDorsal(inscriptionId, VALID_CREDIT_CARD_NUMBER);
            Inscription inscriptionA = findInscriptionS(inscriptionId, VALID_CREDIT_CARD_NUMBER);

            inscriptionA.equals(findInscriptionS(inscriptionId, VALID_CREDIT_CARD_NUMBER).isDorsalTaken());

        } catch (MaxParticipantsException e) {
            e.printStackTrace();
        } catch (DorsalAlreadyTakenException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (InscriptionNotFoundException e) {
            e.printStackTrace();
        } catch (InputValidationException e) {
            e.printStackTrace();
        } catch (LateInscriptionException e) {
            e.printStackTrace();
        } catch (RunCelebrationException e) {
            e.printStackTrace();
        } catch (IncorrectCreditCardException e) {
            e.printStackTrace();
        } finally {
            removeRun(run.getRunId());
        }
    }

    @Test   // Test if the Dorsal has been already taken
    public void testTakeDorsalAlreadyTaken() {
        Run run = createRun(getValidRun());
        try {
            Long inscriptionId = runService.registerInRun(run.getRunId(), VALID_MAIL, VALID_CREDIT_CARD_NUMBER);

            // Everything must be fine
            runService.takeDorsal(inscriptionId, VALID_CREDIT_CARD_NUMBER);

            // Trying again...
            assertThrows(DorsalAlreadyTakenException.class, () -> {
                runService.takeDorsal(inscriptionId, VALID_CREDIT_CARD_NUMBER);
            });

        } catch (MaxParticipantsException e) {
            e.printStackTrace();
        } catch (DorsalAlreadyTakenException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (InscriptionNotFoundException e) {
            e.printStackTrace();
        } catch (InputValidationException e) {
            e.printStackTrace();
        } catch (LateInscriptionException e) {
            e.printStackTrace();
        } catch (RunCelebrationException e) {
            e.printStackTrace();
        } catch (IncorrectCreditCardException e) {
            e.printStackTrace();
        } finally {
            removeRun(run.getRunId());
        }
    }

}
