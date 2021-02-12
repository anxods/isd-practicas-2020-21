package es.udc.ws.app.model.Inscription;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.Constructor;

/**
 * A factory to get
 * <code>SqlInscriptionDao</code> objects. <p> Required es.udc.es.es.udc.ws.app.model.util.configuration parameters: <ul>
 * <li><code>SqlInscriptionDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlInscriptionDao</code>.</li> </ul>
 */
public class SqlInscriptionDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlInscriptionDaoFactory.className";
    private static SqlInscriptionDao dao = null;

    private SqlInscriptionDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlInscriptionDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            Constructor c = daoClass.getDeclaredConstructor();
            System.out.println(c.getParameterTypes().length);
            return (SqlInscriptionDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlInscriptionDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
