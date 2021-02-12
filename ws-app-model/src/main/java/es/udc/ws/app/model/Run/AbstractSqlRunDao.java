package es.udc.ws.app.model.Run;

import es.udc.ws.app.model.Inscription.Inscription;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlRunDao implements SqlRunDao {

    protected AbstractSqlRunDao(){
    }

    @Override
    public Run find(Connection connection, Long runId)
            throws InstanceNotFoundException {

        String queryString = "SELECT runLocation, runDescription, "
                + "runStartDate, runPrice, runMaxParticipants, "
                + "runParticipants, runCreationDate FROM Run WHERE runId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            /* Fill prepared Statement*/
            int i = 1;
            preparedStatement.setLong(i++, runId.longValue());

            /*Execute query*/
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new InstanceNotFoundException(runId,Run.class.getName());
            }

            /*Get results. */
            i = 1;
            String runLocation = resultSet.getString(i++);
            String runDescription = resultSet.getString(i++);
            Timestamp runDateAsTimestamp_runStartDate = resultSet.getTimestamp(i++);
            LocalDateTime runStartDate = runDateAsTimestamp_runStartDate != null
                    ? runDateAsTimestamp_runStartDate.toLocalDateTime()
                    :null;
            float runPrice = resultSet.getFloat(i++);
            int runMaxParticipants = resultSet.getInt(i++);
            int runParticipants = resultSet.getInt(i++);
            Timestamp runDateAsTimestamp_runCreationDate = resultSet.getTimestamp(i++);
            LocalDateTime runCreationDate = runDateAsTimestamp_runCreationDate != null
                    ? runDateAsTimestamp_runCreationDate.toLocalDateTime()
                    :null;

            /* Return run */
            return new Run(runId, runLocation, runDescription, runStartDate,
                 runPrice, runMaxParticipants, runParticipants, runCreationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Run run)
            throws InstanceNotFoundException{

        /* Create "queryString" */
        String queryString = "UPDATE Run "
                + "SET runLocation = ?, runDescription = ?,"
                + " runStartDate = ?, runPrice = ?, runMaxParticipants = ?,"
                + " runParticipants = ? WHERE runId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            /*Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, run.getRunLocation());
            preparedStatement.setString(i++, run.getRunDescription());

            Timestamp startdate = run.getRunStartDate() != null
                    ? Timestamp.valueOf(run.getRunStartDate()) : null;
            preparedStatement.setTimestamp(i++, startdate);

            preparedStatement.setFloat(i++, run.getRunPrice());
            preparedStatement.setInt(i++, run.getRunMaxParticipants());
            preparedStatement.setInt(i++, run.getRunParticipants());

            preparedStatement.setLong(i++, run.getRunId());

            /* Execute query */
            int updateRows = preparedStatement.executeUpdate();

            if(updateRows == 0){
                throw new InstanceNotFoundException(run.getRunId(),
                        Run.class.getName());
            }


        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long runId)
            throws InstanceNotFoundException{

        /*Create "queryString". */
        String queryString = "DELETE FROM Run WHERE" + " runId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            /*Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, runId);

            /*Execute query*/
            int removedRows = preparedStatement.executeUpdate();

            if(removedRows == 0){
                throw new InstanceNotFoundException(runId,
                        Run.class.getName());
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Run> findByKeywords(Connection connection, String city, String date) {
        /* Create "queryString". */

        String queryString = "SELECT runId, runLocation,"
                + " runDescription, runStartDate, runPrice, runMaxParticipants,"
                + " runParticipants, runCreationDate FROM Run";
        queryString += " WHERE runLocation = ?";
        if (date != null) {
            queryString += " AND runStartDate <= ?";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /*Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, city);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            var data = LocalDateTime.parse(date, formatter);
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(data));

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read runs. */
            List<Run> runs = new ArrayList<Run>();

            while (resultSet.next()) {

                i = 1;
                Long runId = resultSet.getLong(i++);
                String runLocation = resultSet.getString(i++);
                String runDescription = resultSet.getString(i++);
                Timestamp runStartDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime runStartDate = runStartDateAsTimestamp != null
                        ? runStartDateAsTimestamp.toLocalDateTime()
                        : null;
                float runPrice = resultSet.getFloat(i++);
                int runMaxParticipants = resultSet.getInt(i++);
                int runParticipants = resultSet.getInt(i++);
                Timestamp runCreationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime runCreationDate = runCreationDateAsTimestamp != null
                        ? runCreationDateAsTimestamp.toLocalDateTime()
                        : null;

                runs.add(new Run(runId, runLocation, runDescription, runStartDate,
                        runPrice, runMaxParticipants, runParticipants, runCreationDate));

            }

            /* Return runs. */
            return runs;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
