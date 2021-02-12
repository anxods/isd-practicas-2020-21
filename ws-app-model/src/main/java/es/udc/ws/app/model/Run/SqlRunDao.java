package es.udc.ws.app.model.Run;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlRunDao {

    public Run create(Connection connection, Run run);

    public Run find(Connection connection, Long runId)
            throws InstanceNotFoundException;

    public List<Run> findByKeywords(Connection connection, String city, String date);

    public void update(Connection connection, Run run)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long runId)
            throws InstanceNotFoundException;

}
