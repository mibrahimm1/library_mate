package com.cargologix.library_mate.database;
import java.sql.*;

public class db {
    private static db handler = null ;
    private static final String DB_URL = "jdbc:mysql://localhost/library_mate";
    private static final String DB_USER = "root"; // MySQL username
    private static final String DB_PASSWORD = ""; // MySQL password
    public static Connection connection = null ;
    private static Statement stmnt = null ;

    private db() throws ClassNotFoundException { // PRIVATE SO THAT NO OTHER CLASS CAN CREATE A NEW INSTANCE OF DATABASE
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssueTable();
        setupReturnTable();
        setupAccountTable();

    }

    public static db getInstance() // METHOD TO BE USED BY CLASSES TO RETRIEVE INSTANCE OF DATABASE
    {
        if (handler == null) {
            try {
                handler = new db() ;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return handler ;
    }

    void createConnection() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setupMemberTable() {
        String TABLE_NAME = "MEMBER" ;
        try {
            stmnt = connection.createStatement() ;

            DatabaseMetaData dbm = connection.getMetaData() ;
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmnt.execute("CREATE TABLE " + TABLE_NAME + " ("
                        + "    id varchar(200) primary key,\n"
                        + "    name varchar(200),\n"
                        + "    phonenumber varchar(200),\n"
                        + "    email varchar(100)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " . . . setupMemberTable");
        }
    }

    void setupAccountTable() {
        String TABLE_NAME = "ACCOUNT" ;
        try {
            stmnt = connection.createStatement() ;

            DatabaseMetaData dbm = connection.getMetaData() ;
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmnt.execute("CREATE TABLE " + TABLE_NAME + " ("
                        + "    username varchar(200) primary key,\n"
                        + "    password varchar(200),\n"
                        + "    isAdmin boolean default 0,\n"
                        + "    id varchar(200) ,\n"
                        + "    FOREIGN KEY (id) REFERENCES MEMBER(id)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " . . . setupAccountTable");
        }
    }

    void setupIssueTable() {
        String TABLE_NAME = "ISSUE" ;
        try {
            stmnt = connection.createStatement() ;

            DatabaseMetaData dbm = connection.getMetaData() ;
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmnt.execute("CREATE TABLE " + TABLE_NAME + " ("
                        + "    bookID varchar(200) primary key,\n"
                        + "    memberID varchar(200),\n"
                        + "    issueTime timestamp default CURRENT_TIMESTAMP,\n"
                        + "    renew_count integer default 0,\n"
                        + "    FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                        + "    FOREIGN KEY (memberID) REFERENCES MEMBER(id)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " . . . setupIssueTable");
        }
    }

    void setupReturnTable() {
        String TABLE_NAME = "RETURN" ;
        try {
            stmnt = connection.createStatement() ;

            DatabaseMetaData dbm = connection.getMetaData() ;
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmnt.execute("CREATE TABLE `" + TABLE_NAME + "` ("
                        + "    bookID varchar(200),\n"
                        + "    memberID varchar(200),\n"
                        + "    issueDate timestamp ,\n"
                        + "    returnDate timestamp default CURRENT_TIMESTAMP,\n"
                        + "    lateSubmitFine integer default 0,\n"
                        + "    FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                        + "    FOREIGN KEY (memberID) REFERENCES MEMBER(id)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " . . . setupReturnTable");
        }
    }


    void setupBookTable() {
        String TABLE_NAME = "BOOK" ;
        try {
            stmnt = connection.createStatement() ;

            DatabaseMetaData dbm = connection.getMetaData() ;
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmnt.execute("CREATE TABLE " + TABLE_NAME + " ("
                        + "    id varchar(200) primary key,\n"
                        + "    title varchar(200),\n"
                        + "    author varchar(200),\n"
                        + "    publisher varchar(100),\n"
                        + "    isAvail boolean default true"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " . . . setupBookTable");
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet result ;
        try {
            stmnt = connection.createStatement() ;
            result = stmnt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null ;
        }
        return result ;
    }

    public boolean executeAction(String q) {
        try {
            stmnt = connection.createStatement();
            stmnt.execute(q);
            return true;
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false ;
        }
    }
}