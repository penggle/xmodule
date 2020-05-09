package com.penglecode.xmodule.common.util;

import java.sql.Driver;
import java.sql.SQLException;

/**
 * JDBC工具类
 * 
 * @author 	pengpeng
 * @date 	2020年4月16日 上午9:55:29
 */
public class JdbcUtils extends org.springframework.jdbc.support.JdbcUtils {

	private static Boolean mysqlDriverVersion6;
	
	public static String getDriverClassName(String rawUrl) throws SQLException {
        if (rawUrl == null) {
            return null;
        }
        
        if (rawUrl.startsWith("jdbc:derby:")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        } else if (rawUrl.startsWith("jdbc:mysql:")) {
            if (mysqlDriverVersion6 == null) {
                mysqlDriverVersion6 = ClassUtils.loadClass("com.mysql.cj.jdbc.Driver") != null;
            }

            if (mysqlDriverVersion6) {
                return "com.mysql.cj.jdbc.Driver";
            } else {
                return "com.mysql.jdbc.Driver";
            }
        } else if (rawUrl.startsWith("jdbc:log4jdbc:")) {
            return "net.sf.log4jdbc.DriverSpy";
        } else if (rawUrl.startsWith("jdbc:mariadb:")) {
            return "org.mariadb.jdbc.Driver";
        } else if (rawUrl.startsWith("jdbc:oracle:") //
                   || rawUrl.startsWith("JDBC:oracle:")) {
            return "oracle.jdbc.OracleDriver";
        } else if (rawUrl.startsWith("jdbc:microsoft:")) {
            return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        } else if (rawUrl.startsWith("jdbc:sqlserver:")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (rawUrl.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        } else if (rawUrl.startsWith("jdbc:edb:")) {
            return "org.postgresql.Driver";
        } else if (rawUrl.startsWith("jdbc:hsqldb:")) {
            return "org.hsqldb.jdbcDriver";
        } else if (rawUrl.startsWith("jdbc:db2:")) {
            return "com.ibm.db2.jcc.DB2Driver";
        } else if (rawUrl.startsWith("jdbc:sqlite:")) {
            return "org.sqlite.JDBC";
        } else if (rawUrl.startsWith("jdbc:ingres:")) {
            return "com.ingres.jdbc.IngresDriver";
        } else if (rawUrl.startsWith("jdbc:h2:")) {
            return "org.h2.Driver";
        } else if (rawUrl.startsWith("jdbc:hive:")) {
            return "org.apache.hive.jdbc.HiveDriver";
        } else if (rawUrl.startsWith("jdbc:hive2:")) {
            return "org.apache.hive.jdbc.HiveDriver";
        } else if (rawUrl.startsWith("jdbc:clickhouse:")) {
            return "ru.yandex.clickhouse.ClickHouseDriver";
        } else {
            throw new SQLException("unknown jdbc driver : " + rawUrl);
        }
    }

    public static String getDbType(String rawUrl, String driverClassName) {
        if (rawUrl == null) {
            return null;
        }

        if (rawUrl.startsWith("jdbc:derby:") || rawUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return "derby";
        } else if (rawUrl.startsWith("jdbc:mysql:") || rawUrl.startsWith("jdbc:cobar:")
                   || rawUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return "mysql";
        } else if (rawUrl.startsWith("jdbc:mariadb:")) {
            return "mariadb";
        } else if (rawUrl.startsWith("jdbc:oracle:") || rawUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return "oracle";
        } else if (rawUrl.startsWith("jdbc:microsoft:") || rawUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return "sqlserver";
        } else if (rawUrl.startsWith("jdbc:sqlserver:") || rawUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return "sqlserver";
        } else if (rawUrl.startsWith("jdbc:postgresql:") || rawUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return "postgresql";
        } else if (rawUrl.startsWith("jdbc:hsqldb:") || rawUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return "hsql";
        } else if (rawUrl.startsWith("jdbc:db2:")) {
            return "db2";
        } else if (rawUrl.startsWith("jdbc:sqlite:")) {
            return "sqlite";
        } else if (rawUrl.startsWith("jdbc:ingres:")) {
            return "ingres";
        } else if (rawUrl.startsWith("jdbc:h2:") || rawUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return "h2";
        } else if (rawUrl.startsWith("jdbc:hive:")) {
            return "hive";
        } else if (rawUrl.startsWith("jdbc:hive2:")) {
            return "hive";
        } else if (rawUrl.startsWith("jdbc:clickhouse:")) {
            return "clickhouse";
        } else {
            return null;
        }
    }

    public static Driver createDriver(String driverClassName) throws SQLException {
        return createDriver(null, driverClassName);
    }

    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e.getMessage(), e);
            }
        }

        try {
            return (Driver) clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
	
}
