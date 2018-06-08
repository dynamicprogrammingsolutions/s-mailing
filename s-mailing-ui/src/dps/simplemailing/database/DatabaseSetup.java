package dps.simplemailing.database;

import dps.commons.startup.Startup;
import dps.logging.HasLogger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import javax.transaction.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

@Startup
@ApplicationScoped
public class DatabaseSetup implements HasLogger {

    @Resource(lookup = "java:comp/env/jdbc/DefaultDS")
    DataSource dataSource;

    @Resource
    UserTransaction tx;

    @PostConstruct
    void init() {

        try {

            tx.begin();

            try (Connection connection = dataSource.getConnection()) {

                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(connection.getCatalog(), connection.getSchema(), null, null);

                System.out.println("getting tables");
                int cnt = 0;
                while (tables.next()) {
                    cnt++;
                    System.out.println(tables.getString("TABLE_NAME"));
                }
                if (cnt == 0) {
                    System.out.println("no tables");

                    connection.createStatement().execute("create table databaseversion (\"version\" int)");
                    connection.createStatement().execute("insert into databaseversion (\"version\") values (0)");

                }

                int version = 0;
                Statement statement1 = connection.createStatement();
                statement1.execute("select \"version\" from databaseversion");
                ResultSet resultSet = statement1.getResultSet();
                if (resultSet.next()) {
                    version = resultSet.getInt(1);
                    int newversion = version;
                    while (true) {
                        newversion++;
                        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("db/version_"+newversion+".ddl");
                        if (resourceAsStream != null) {
                            System.out.println("execute migration version "+newversion);

                            try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(resourceAsStream))) {

                                String line;
                                while ((line = inputStreamReader.readLine()) != null) {
                                    System.out.println("executing: " + line);
                                    Statement statement = connection.createStatement();
                                    statement.execute(line);
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            newversion--;
                            break;
                        }
                    }

                    if (version != newversion) {
                        System.out.println("new version: "+newversion);
                        connection.createStatement().execute("update databaseversion set \"version\" = "+newversion);
                    }

                } else {
                    throw new RuntimeException("couldn't find database version");
                }

            }

        } catch (SQLException | NotSupportedException | SystemException e) {
            e.printStackTrace();
            try {
                tx.setRollbackOnly();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                tx.commit();
            } catch (HeuristicMixedException | SystemException | RollbackException | HeuristicRollbackException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    boolean createTableIfNotExists(Connection connection, String tableName, String... sql) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, null);
        boolean exists = false;
        while (tables.next()) {
            if (tables.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            logInfo(tableName+" EXISTS");
            return false;
        } else {
            logInfo(tableName+" NOT EXISTS");
            for (String currentSql: sql) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(currentSql);
            }
            return true;
        }
    }

    boolean createColumnIfNotExists(Connection connection, String tableName, String colName, String... sql) throws SQLException
    {
        return createOrDropColumnIf(connection,tableName,true,colName,sql);
    }

    boolean dropColumnIfExists(Connection connection, String tableName, String colName, String... sql) throws SQLException
    {
        return createOrDropColumnIf(connection,tableName,false,colName,sql);
    }

    boolean createOrDropColumnIf(Connection connection, String tableName, boolean shouldExist, String colName, String... sql) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet articleTableResultSetLower = metaData.getTables(null, null, tableName.toLowerCase(), null);
        ResultSet articleTableResultSetUpper = metaData.getTables(null, null, tableName.toUpperCase(), null);

        boolean tableFound = false;
        if (articleTableResultSetLower.next()) {
            tableName = tableName.toLowerCase();
            tableFound = true;
        } else if (articleTableResultSetUpper.next()) {
            tableName = tableName.toUpperCase();
            tableFound = true;
        }

        if (tableFound) {
            logInfo(tableName+" EXISTS");

            ResultSet columnResultSetLower = metaData.getColumns(null, null, tableName, colName.toLowerCase());
            ResultSet columnResultSetUpper = metaData.getColumns(null, null, tableName, colName.toUpperCase());
            if ((columnResultSetLower.next() || columnResultSetUpper.next()) == shouldExist) {
                logInfo(tableName+"."+colName+" EXISTS");
                return false;
            } else {
                logInfo(tableName+"."+colName+" NOT EXISTS");
                for (String currentSql: sql) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(currentSql);
                }
                return true;
            }

        } else {
            throw new SQLException(tableName+" NOT EXISTS");
        }

    }

}
