package com.revolut.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2ConnectionManager {

    private static H2ConnectionManager INSTANCE = null;

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/revolut-test";

    private static final String user = "sa";
    private static final String pass = "";

    private static Connection conn;

    private H2ConnectionManager(){
        //Singleton instance.
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, user, pass);

            System.out.println("Creating ACCOUNTS table ...");
            Statement stmt = conn.createStatement();
            String dropSQL = "DROP TABLE IF EXISTS ACCOUNTS";
            String createSQL =  "CREATE TABLE ACCOUNTS " +
                    "(accountNumber LONG not NULL, " +
                    " balance VARCHAR(255), " +
                    " PRIMARY KEY ( accountNumber ))";
            stmt.addBatch(dropSQL);
            stmt.addBatch(createSQL);
            stmt.executeBatch();
            System.out.println("Created table in given database...");

            stmt.close();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static H2ConnectionManager getInstance(){
        if(INSTANCE == null){
            synchronized (H2ConnectionManager.class){
                INSTANCE = new H2ConnectionManager();
            }
        }

        return INSTANCE;
    }

    public Connection getDbConnection(){
        return conn;
    }


//    public Account fetchAccount(Long accountNumber){
//        try {
//            Statement stmt = conn.createStatement();
//            String SQL = "SELECT * FROM ACCOUNTS";
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
