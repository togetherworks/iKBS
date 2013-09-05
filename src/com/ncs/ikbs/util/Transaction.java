package com.ncs.ikbs.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * Description:  Allow transaction control to span over methods.
 *               This class will get a connection from the connection pool and
 *               give it to the db class to use. Transaction object may be passed
 *               from one method to another.
 *               Should be created in control classes and pass into db classes
 * Copyright:    Copyright (c) 2002
 * Company:
 * @version 1.0
 */

public class Transaction {
    
  private Connection conn;
  private boolean autocommit = true;

  /**
   * @param autocommit - false
   * When set to flase, this txn object may span over a few methods
   * MUST be accompanied by commit() at the end of all db sub-transactions
   */

  // Adam Wilmore - Method Overridden to use Tomcat Managed DataSource
  
//  public Transaction(boolean autocommit){
//    try{
//      //grab a connection from the pool
//      this.conn = DbManager.getInstance().getPool().getConnection();
//      if(conn == null){
//		System.out.println("Conn is null");
//        throw new SQLException ("Cannot get a connection from pool");
//      }else{
//        this.autocommit= autocommit;
//        conn.setAutoCommit(autocommit);
//      }
//
//    }catch(SQLException e){
//      System.out.println(e + "\nCannot create a transaction");
//    }
//  }
  
  public Transaction(boolean autoCommit) 
  		throws SQLException {
      
      this.autocommit = autoCommit;
      
      try {
	      if(conn == null) {
	          
	          Context ctx = new InitialContext();
	          
	          if(ctx != null) {
		          
		          DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/ikbsDS");
		          
		          if(ds != null) {
		              conn = ds.getConnection();
		              conn.setAutoCommit(autoCommit);
		          }
	          }
	      }
	      
      } catch(NamingException e) {
          e.printStackTrace();
      }
  }

  /**
   * If no arg, auto-commit for the connection object is used.
   * For reading type of db methods or single updates.
   * Must follow by commit()
   */
  public Transaction()
  		throws SQLException {
      
    this(true);
  }

  /**
   *Commit the transaction (and the underlying connection) and return the
   *connection to the pool.
   */

   public void commit(){
    try{
      if(!autocommit){
        conn.commit();
      }
      
      // Adam Wilmore 
      //DbManager.getInstance().getPool().returnConnection(conn);
    }catch(SQLException e){
      System.out.println("Cannot commit transaction\n" + e);
    }
   }

   /**
    * Same idea as Connection.roolback(). Use with manual commit
    * where autocommit is set to false
    */

    public void rollBack(){
      try{
        conn.rollback();
      }catch(SQLException e){
        System.out.println("Cannot roll back transaction\n" + e );
      }
    }

    /**
     * Return the connection associated with the txn
     */

     public Connection getConnection(){
       return conn;
     }
    
    
}