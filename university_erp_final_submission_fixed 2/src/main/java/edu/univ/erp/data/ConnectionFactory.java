package edu.univ.erp.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.sql.DataSource;

public class ConnectionFactory {
    private static HikariDataSource authDs;
    private static HikariDataSource erpDs;

    public static void initFromFile(String propsPath) throws Exception {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(propsPath)) { p.load(in); }
        init(p);
    }

    public static void init(Properties p) {
        HikariConfig ac = new HikariConfig();
        ac.setJdbcUrl(p.getProperty("auth.jdbc.url"));
        ac.setUsername(p.getProperty("auth.jdbc.username"));
        ac.setPassword(p.getProperty("auth.jdbc.password"));
        ac.setMaximumPoolSize(Integer.parseInt(p.getProperty("db.pool.maximumPoolSize", "10")));
        authDs = new HikariDataSource(ac);

        HikariConfig ec = new HikariConfig();
        ec.setJdbcUrl(p.getProperty("erp.jdbc.url"));
        ec.setUsername(p.getProperty("erp.jdbc.username"));
        ec.setPassword(p.getProperty("erp.jdbc.password"));
        ec.setMaximumPoolSize(Integer.parseInt(p.getProperty("db.pool.maximumPoolSize", "10")));
        erpDs = new HikariDataSource(ec);
    }

    public static DataSource authDataSource() { return authDs; }
    public static DataSource erpDataSource() { return erpDs; }
}
