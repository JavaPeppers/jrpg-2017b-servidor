package dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ThreadLocalSessionContext;


public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {

		try {
			sessionFactory = new Configuration().configure()
			.buildSessionFactory();

		} catch (Throwable ex) {

			System.err.println("Initial SessionFactory creation failed." + ex);

			throw new ExceptionInInitializerError(ex);
		}

	}
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public static void openThreadSession() {
   	 Session session = sessionFactory.openSession();
   	 ThreadLocalSessionContext.bind(session);
    }
	
	 public static void closeThreadSession() {
	        Session session = ThreadLocalSessionContext.unbind(sessionFactory);
	        if (session!=null)
	            session.close();
	 }
}