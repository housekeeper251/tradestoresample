package com.ourcompany.tradestore.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public class DatabaseConnectionUtil {
	
	private static DatabaseConnectionUtil singletonInstance = new DatabaseConnectionUtil();
	
	private static EntityManagerFactory factory = null;
	
	public EntityManager openDatabaseConnection(){
		if(factory==null){
			factory = Persistence.createEntityManagerFactory("TradeStore");
		}
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		
		return em;
	}
	
	public void rollback(EntityManager em) {
		em.getTransaction().rollback();
	}

	public void commit(EntityManager em) {
		em.getTransaction().commit();
	}

	public void close(EntityManager em) {
		em.clear();
		em.close();
	}

	
	public static DatabaseConnectionUtil getInstance(){
		return singletonInstance;
	}
	

	private static class MyWork implements Work {

	    Connection conn;

	    //@Override
	    public void execute(Connection arg0) throws SQLException {
	        this.conn = arg0;
	    }

	    Connection getConnection() {
	        return conn;
	    }

	}
	
	public static Connection getRawSqlConnection(EntityManager em){
		
	    Session session = em.unwrap(Session.class);
        MyWork myWork = new MyWork();
        session.doWork(myWork);
        return myWork.getConnection();
	}

}
