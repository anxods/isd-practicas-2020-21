package es.udc.ws.app.model.Inscription;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlInscriptionDao implements SqlInscriptionDao{

    public AbstractSqlInscriptionDao(){
    }

    @Override
    public Inscription find(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException {

        String queryString = "SELECT runId, inscriptionDorsal,"
                + " inscriptionUserEmail, inscriptionCreditCardNumber,"
                + " inscriptionDate, isDorsalTaken FROM Inscription WHERE inscriptionId = ?";


        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscriptionId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(inscriptionId,
                        Inscription.class.getName());
            }

            /* Get results. */
            i = 1;
            Long runId = resultSet.getLong(i++);
            String inscriptionDorsal = resultSet.getString(i++);
            String inscriptionUserEmail = resultSet.getString(i++);
            String inscriptionCreditCardNumber = resultSet.getString(i++);
            Timestamp inscriptionDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime inscriptionDate = inscriptionDateAsTimestamp != null
                    ? inscriptionDateAsTimestamp.toLocalDateTime()
                    : null;
            boolean isDorsalTaken = resultSet.getBoolean(i++);

            /* Return inscription. */
            return new Inscription(inscriptionId, runId, inscriptionDorsal,
                     inscriptionUserEmail, inscriptionCreditCardNumber, inscriptionDate, isDorsalTaken);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Inscription> findByEmail(Connection connection, String email) {

        String queryString = "SELECT inscriptionId, runId, inscriptionDorsal," +
                " inscriptionCreditCardNumber, inscriptionDate, isDorsalTaken" +
                " FROM Inscription WHERE inscriptionUserEmail = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Inscription> inscriptions = new ArrayList<Inscription>();

            while (resultSet.next()) {

                i = 1;
                Long inscriptionId = Long.valueOf(resultSet.getLong(i++));
                Long runId = Long.valueOf(resultSet.getLong(i++));
                String inscriptionDorsal = resultSet.getString(i++);
                String inscriptionCreditCardNumber = resultSet.getString(i++);
                Timestamp inscriptionDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime inscriptionDate = inscriptionDateAsTimestamp.toLocalDateTime();
                boolean isDorsalTaken = resultSet.getBoolean(i++);

                inscriptions.add(new Inscription(inscriptionId, runId, inscriptionDorsal, email,
                        inscriptionCreditCardNumber, inscriptionDate, isDorsalTaken));

            }

            return inscriptions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Inscription inscription)
            throws InstanceNotFoundException{

        /* Create "queryString". */
        String queryString = "UPDATE Inscription"
                + " SET runId = ?, inscriptionDorsal = ?, inscriptionUserEmail = ?, "
                + " inscriptionCreditCardNumber = ?, inscriptionDate = ?, isDorsalTaken = ? WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscription.getRunId());
            preparedStatement.setString(i++, inscription.getInscriptionDorsal());
            preparedStatement.setString(i++, inscription.getInscriptionUserEmail());
            preparedStatement.setString(i++, inscription.getInscriptionCreditCardNumber());
            Timestamp date = inscription.getInscriptionDate() != null
                    ? Timestamp.valueOf(inscription.getInscriptionDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setBoolean(i++, inscription.isDorsalTaken());

            preparedStatement.setLong(i++, inscription.getInscriptionId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(inscription.getRunId(),
                        Inscription.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Inscription WHERE" + " inscriptionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscriptionId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(inscriptionId,
                        Inscription.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
