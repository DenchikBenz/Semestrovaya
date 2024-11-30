package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class DatabaseConnection {
    private static DataSource dataSource;

    static {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[] {"localhost"});
        ds.setPortNumbers(new int[] {5432});
        ds.setDatabaseName("Semestrovaya");
        ds.setUser("danilbezin");
        ds.setPassword("postgres");
        dataSource = ds;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

