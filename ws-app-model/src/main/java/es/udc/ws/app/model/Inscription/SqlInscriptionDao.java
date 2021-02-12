package es.udc.ws.app.model.Inscription;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.app.model.Run.Run;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlInscriptionDao {

    public Inscription create(Connection connection, Inscription inscription);

    public Inscription find(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException;

    public List<Inscription> findByEmail(Connection connection, String email);

    public void update(Connection connection, Inscription inscription)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException;

}
