package es.udc.ws.app.model.Inscription;

import java.sql.*;

public class Jdbc3CcSqlInscriptionDao extends AbstractSqlInscriptionDao {

    @Override
    public Inscription create(Connection connection, Inscription inscription) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Inscription"
                + " (runId, inscriptionDorsal, inscriptionUserEmail,"
                + " inscriptionCreditCardNumber, inscriptionDate, isDorsalTaken)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscription.getRunId());
            preparedStatement.setString(i++, inscription.getInscriptionDorsal());
            preparedStatement.setString(i++, inscription.getInscriptionUserEmail());
            preparedStatement.setString(i++, inscription.getInscriptionCreditCardNumber());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(inscription.getInscriptionDate()));
            preparedStatement.setBoolean(i++, inscription.isDorsalTaken());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key."
                );
            }
            Long inscriptionId = resultSet.getLong(1);

            /* Return inscription. */
            return new Inscription(inscriptionId, inscription.getRunId(),
                    inscription.getInscriptionDorsal(), inscription.getInscriptionUserEmail(),
                    inscription.getInscriptionCreditCardNumber(), inscription.getInscriptionDate(),
                    inscription.isDorsalTaken()
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
