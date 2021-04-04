package persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            config.setJdbcUrl("jdbc:mysql://thomas-db.c64ylwt4ybkn.us-east-1.rds.amazonaws.com:3306/purchase");
            config.setUsername("admin");
            config.setPassword("icecoldnewenglandipa");
            config.setDriverClassName("com.mysql.jdbc.Driver");
            ds = new HikariDataSource(config);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DbConnection(){}
}

