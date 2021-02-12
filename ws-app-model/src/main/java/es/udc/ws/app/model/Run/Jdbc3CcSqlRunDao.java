package es.udc.ws.app.model.Run;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

public class Jdbc3CcSqlRunDao extends AbstractSqlRunDao {

    @Override
    public Run create(Connection connection, Run run) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Run"
                + " (runLocation, runDescription, runStartDate,"
                + " runPrice, runMaxParticipants, runParticipants, runCreationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, run.getRunLocation());
            preparedStatement.setString(i++, run.getRunDescription());
			Timestamp date = run.getRunStartDate() != null ? Timestamp.valueOf(run.getRunStartDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setFloat(i++, run.getRunPrice());
            preparedStatement.setInt(i++, run.getRunMaxParticipants());
            preparedStatement.setInt(i++, run.getRunParticipants());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(run.getRunCreationDate()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long runId = resultSet.getLong(1);

            /* Return run */
            return new Run(runId, run.getRunLocation(), run.getRunDescription(),
                    run.getRunStartDate(), run.getRunPrice(), run.getRunMaxParticipants(),
                    run.getRunParticipants(), run.getRunCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
