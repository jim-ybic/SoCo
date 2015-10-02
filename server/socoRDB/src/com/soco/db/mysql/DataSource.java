package com.soco.db.mysql;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {

    private static DataSource     datasource;
    private ComboPooledDataSource cpds;
    //
    private String _db = "socoserver1";
    private String _port = "3306";
    private String _jdbc_url = "jdbc:mysql://localhost";
    private String _user = "john";
    private String _password = "123";

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl(_jdbc_url + ":" + _port + "/" + _db);
        cpds.setUser(_user);
        cpds.setPassword(_password);

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}
