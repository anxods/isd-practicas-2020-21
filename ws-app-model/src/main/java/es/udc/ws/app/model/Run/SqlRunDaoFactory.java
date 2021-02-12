package es.udc.ws.app.model.Run;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRunDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlRunDaoFactory.className";
    private static SqlRunDao dao = null;

    private SqlRunDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlRunDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRunDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlRunDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
