package com.ncs.ikbs.util;
/**
 * Description:  To initiate the db driver parameters and connection pool can be improved by putting parameters in file or system properties
 * Copyright:    Copyright (c) 2002
 * Company:
 * @version 1.0
 */

public class DbManager{
  private static DbManager instance = null;
  private ConnectionPool connectionPool;

  public static DbManager getInstance(){
    if(instance == null){
      instance = new DbManager();
    }
    return instance;
  }

  //Ref: Taken from "Inside servlet: Chp 16"

  private DbManager(){
	String jdbcDriver = "sun.jdbc.odbc.JdbcOdbcDriver";    
   	String dbURL = "jdbc:odbc:Bubble";
	String password = "sa";
	String username = "sa";

    try{
      //instantiate the connection pool object by passing the
      //jdbc driver, database URL, username, and password

      connectionPool = new ConnectionPool(jdbcDriver,dbURL,username,password);

      //specify the initial number of connections to establish
      connectionPool.setInitialConnections(5);

      //specify the number of incremental connections to create if
      //pool is exhausted of available connections
      connectionPool.setIncrementalConnections(5);

      //specify absolute maximum number of connections to create
      connectionPool.setMaxConnections(100);

      //specify a database table that can be used to validate the database
      //connections (optional)
      //connectionPool.setTestTable("reservation");
      connectionPool.createPool(); //create the pool connection
    }catch(Exception e){
      System.out.println("Error: " + e);
    }
  }

  //Return the pool fot this application
  public ConnectionPool getPool(){
    return connectionPool;
  }
}