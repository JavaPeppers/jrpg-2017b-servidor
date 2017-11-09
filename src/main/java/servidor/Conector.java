package servidor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import hibernateUtil.HibernateUtil;
import inventario.Inventario;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ThreadLocalSessionContext;

import dominio.Item;

/**
 * Clase que se encarga de la comunicación con la base de datos.
 */
public class Conector {
	
	
    /** url de la base de datos. **/
    private String url = "primeraBase.bd";

    /** Variable que se utilizará para la conexión. **/
    private Connection connect;

    /** Variable que indica el parámetro 1 a enviar. **/
    private static final int PARAM1 = 1;

    /** Variable que indica el parámetro 2 a enviar. **/
    private static final int PARAM2 = 2;

    /** Variable que indica el parámetro 3 a enviar. **/
    private static final int PARAM3 = 3;

    /** Variable que indica el parámetro 4 a enviar. **/
    private static final int PARAM4 = 4;

    /** Variable que indica el parámetro 5 a enviar. **/
    private static final int PARAM5 = 5;

    /** Variable que indica el parámetro 6 a enviar. **/
    private static final int PARAM6 = 6;

    /** Variable que indica el parámetro 7 a enviar. **/
    private static final int PARAM7 = 7;

    /** Variable que indica el parámetro 8 a enviar. **/
    private static final int PARAM8 = 8;

    /** Variable que indica el parámetro 9 a enviar. **/
    private static final int PARAM9 = 9;

    /** Variable que indica el parámetro 10 a enviar. **/
    private static final int PARAM10 = 10;

    /** Variable que indica el parámetro 11 a enviar. **/
    private static final int PARAM11 = 11;

    /** Variable que indica el parámetro 12 a enviar. **/
    private static final int PARAM12 = 12;

    /** Variable que indica el parámetro 13 a enviar. **/
    private static final int PARAM13 = 13;

    /** Variable que indica el parámetro 14 a enviar. **/
    private static final int PARAM14 = 14;

    /** Variable que indica el parámetro 15 a enviar. **/
    private static final int PARAM15 = 15;

    /** Variable que indica el parámetro 16 a enviar. **/
    private static final int PARAM16 = 16;

    /** Variable que indica el parámetro 17 a enviar. **/
    private static final int PARAM17 = 17;

    /** Variable que indica el parámetro 21 a enviar. **/
    private static final int PARAM21 = 21;

    /** Variable que indica la cant de items disponibles. **/
    private static final int CANTITEMS = 9;

    /** Variable que indica la cant de items máximos en la mochila. **/
    private static final int CANTITEMSMAXMOCHILA = 20;

    /**
     * Método que establece la conexión con la base de datos.
     */
    public void connect() {
        try {
            Servidor.log.append("Estableciendo conexión "
                  + "con la base de datos..." + System.lineSeparator());
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
            Servidor.log.append("Conexión con la base de datos "
                  + "establecida con éxito." + System.lineSeparator());
            } catch (SQLException ex) {
            Servidor.log.append("Fallo al intentar establecer la conexión"
                    + "con la base de datos. " + ex.getMessage()
                    + System.lineSeparator());
            }
        }

    /** Método que cierra la conexión con la base de datos. **/
    public void close() {
        try {
            connect.close();
            } catch (SQLException ex) {
            Servidor.log.append("Error al intentar cerrar la conexión"
                   + "con la base de datos." + System.lineSeparator());
            Logger.getLogger(Conector.class.getName())
                 .log(Level.SEVERE, null, ex);
         }
     }
    
   

     /**
     * Método encargado de registrar el usuario en la base de datos.
     * @param user PaqueteUsuario con los datos del usuario a registrar.
     * @return boolean Resultado del registro.
     */
    
     
     
    
     public boolean registrarUsuario(PaqueteUsuario user) {
 		//Preparo sesion de hibernate
 		Configuration cfg = new Configuration();
 		cfg.configure("hibernate.cfg.xml");
 		SessionFactory factory = cfg.buildSessionFactory();
 		Session session = factory.openSession();
 		
 		HibernateUtil.openThreadSession(session);
 		
 		//Preparo el criteria
 		CriteriaBuilder cBuilder = session.getCriteriaBuilder();
 		CriteriaQuery<PaqueteUsuario> cQuery = cBuilder.createQuery(PaqueteUsuario.class);
 		Root<PaqueteUsuario> root = cQuery.from(PaqueteUsuario.class);
 		
 		//Ejecuto la query buscando usuarios con ese nombre
 		cQuery.select(root).where(cBuilder.equal(root.get("username"), user.getUsername()));
 		
 		//Si no existen usuarios con ese nombre
 		if(session.createQuery(cQuery).getResultList().isEmpty()) {
 			//Registro el usuario
 			Transaction transaccion = session.beginTransaction();
 			try{
 				session.save(user);
 				transaccion.commit();
 				
 			} catch (HibernateException e) {
 				//Si falló, hago un rollback de la transaccion, cierro sesion, escribo el log y me voy
 				if (transaccion != null)
 					transaccion.rollback();
 				e.printStackTrace();
 				
 				HibernateUtil.closeThreadSession(session, factory);
 				
 				
 				
 				
 				
 				Servidor.log.append("Eror al intentar registrar el usuario " + user.getUsername() + System.lineSeparator());
 				return false;
 			}
 		} else {
 			//Si ya existe un usuario con ese nombre, cierro sesion, escribo el log y me voy
 			HibernateUtil.closeThreadSession(session, factory);	
 			
 			
 			
 			
 			
 			Servidor.log.append("El usuario " + user.getUsername() + " ya se encuentra en uso." + System.lineSeparator());
 			return false;
 		}
 		
 		
 		HibernateUtil.closeThreadSession(session, factory);
 		
 		
 		
 		
 		
 		Servidor.log.append("El usuario " + user.getUsername() + " se ha registrado." + System.lineSeparator());
 		return true;
     }
     
     public boolean registrarPersonaje(PaquetePersonaje pj , PaqueteUsuario user) {
  		
    	//Preparo sesion de hibernate
  		Configuration cfg = new Configuration();
  		cfg.configure("hibernate.cfg.xml");
  		SessionFactory factory = cfg.buildSessionFactory();
  		Session session = factory.openSession();
    	
  		HibernateUtil.openThreadSession(session);
  		
    	//Seteo stats del personaje
    	pj.setAlianza(-1);
    	pj.setExperiencia(0);
    	pj.setIdInventario(pj.getId());
    	pj.setIdMochila(pj.getId());
    	pj.setNivel(1);
    	 
    	Transaction transaccion = session.beginTransaction();
		try{
			pj.setId((Integer) session.save(pj));
			
			transaccion.commit();
			System.out.println(pj.getId());
			user.setIdPj(pj.getId());
			
			actualizarUsuario(user);
			
			//Salida por true o false dependiendo de como
			if (this.registrarInventarioMochila(pj.getId())) {
				Servidor.log.append("El usuario " + user.getUsername() + " ha creado el personaje "
						+ pj.getId() + System.lineSeparator());
				
				HibernateUtil.closeThreadSession(session, factory);
				
				
				
				return true;
			} else {
				
				HibernateUtil.closeThreadSession(session, factory);
				
				session.close();
				factory.close();
				
				Servidor.log.append("Error al registrar la mochila y el inventario del usuario " + user.getUsername() + " con el personaje" + pj.getId() + System.lineSeparator());
				return false;
			}
		} catch (HibernateException e) {
			//Si falló, hago un rollback de la transaccion, cierro sesion, escribo el log y me voy
			if (transaccion != null)
				transaccion.rollback();
			e.printStackTrace();
			
			HibernateUtil.closeThreadSession(session, factory);
			
			
			Servidor.log.append(
					"Error al intentar crear el personaje " + pj.getNombre() + System.lineSeparator());
			return false;
		}
  }
     
     public void actualizarUsuario(PaqueteUsuario user) {
    	//Preparo sesion de hibernate
  		Configuration cfg = new Configuration();
  		cfg.configure("hibernate.cfg.xml");
  		SessionFactory factory = cfg.buildSessionFactory();
  		Session session = factory.openSession();
  		
  		HibernateUtil.openThreadSession(session);
  		
  		//Preparo el criteria
  		CriteriaBuilder cBuilder = session.getCriteriaBuilder();
  		CriteriaQuery<PaqueteUsuario> cQuery = cBuilder.createQuery(PaqueteUsuario.class);
  		Root<PaqueteUsuario> root = cQuery.from(PaqueteUsuario.class);
  		
  		
  		//Actualizo el usuario
  		Transaction transaccion = session.beginTransaction();
  		session.update(user);
  		transaccion.commit();
  		
  		HibernateUtil.closeThreadSession(session, factory);
  		
  		
  		
     }

    /**
     * Método encargado de registrar el pesonaje en la base de datos.
     * @param paquetePersonaje PaquetePersonaje con los datos
     * del personaje a registrar
     * @param paqueteUsuario PaqueteUsuario con los datos del
     * usuario al que hay que registrarle el personaje
     * @return boolean
     */
     
     
    
     
    /**
     * Método que registra el inventario del personaje.
     * @param idInventarioMochila identificador del inventario
     * @return boolean
     */
    public boolean registrarInventarioMochila2(final int idInventarioMochila) {
    	//Preparo sesion de hibernate
    	Configuration cfg = new Configuration();
    	cfg.configure("hibernate.cfg.xml");
    	SessionFactory factory = cfg.buildSessionFactory();
    	Session session = factory.openSession();
  		
    	HibernateUtil.openThreadSession(session);
  		
    	Inv inv = new Inv();
    	inv.setaccesorio(-1);
    	inv.setcabeza(-1);
    	inv.setidInventario(idInventarioMochila);
    	inv.setmanos2(-1);
    	inv.setmanos1(-1);
    	inv.setpecho(-1);
    	inv.setaccesorio(-1);
    	inv.setpie(-1);
    	
    	
    	
    	//Preparo el criteria
    	CriteriaBuilder cBuilder = session.getCriteriaBuilder();
    	CriteriaQuery<Inv> cQuery = cBuilder.createQuery(Inv.class);
    	Root<Inv> root = cQuery.from(Inv.class);
  		
  		
    	
    	
  		//Actualizo el usuario
  		Transaction transaccion = session.beginTransaction();
  		session.save(inv);
  		transaccion.commit();
  		
  		HibernateUtil.closeThreadSession(session, factory);
  		
  		Configuration cfg2 = new Configuration();
    	cfg2.configure("hibernate.cfg.xml");
    	SessionFactory factory2 = cfg2.buildSessionFactory();
    	Session session2 = factory2.openSession();
  		
    	HibernateUtil.openThreadSession(session2);
  		
    	mochila mochila = new mochila();
    	mochila.setIdMochila(idInventarioMochila);
    	mochila.setItem1(-1);
    	mochila.setItem2(-1);
    	mochila.setItem3(-1);
    	mochila.setItem4(-1);
    	mochila.setItem5(-1);
    	mochila.setItem6(-1);
    	mochila.setItem7(-1);
    	mochila.setItem8(-1);
    	mochila.setItem9(-1);
    	mochila.setItem10(-1);
    	mochila.setItem11(-1);
    	mochila.setItem12(-1);
    	mochila.setItem13(-1);
    	mochila.setItem14(-1);
    	mochila.setItem15(-1);
    	mochila.setItem16(-1);
    	mochila.setItem17(-1);
    	mochila.setItem18(-1);
    	mochila.setItem19(-1);
    	mochila.setItem20(-1);
    	
    	
    	
    	//Preparo el criteria
    	CriteriaBuilder cBuilder2 = session2.getCriteriaBuilder();
    	CriteriaQuery<mochila> cQuery2 = cBuilder2.createQuery(mochila.class);
    	Root<mochila> root2 = cQuery2.from(mochila.class);
  		
  		
    	
    	
  		//Actualizo el usuario
  		Transaction transaccion2 = session2.beginTransaction();
  		session2.save(mochila);
  		transaccion2.commit();
  		
  		HibernateUtil.closeThreadSession(session2, factory2);
  		
  		return true;
  		
  		
  		
     }

    
     
    public boolean registrarInventarioMochila(final int idInventarioMochila) {
        try {
            // Preparo la consulta para el registro el inventario en la base de
            // datos
            PreparedStatement stRegistrarInventario = connect.prepareStatement(
                  "INSERT INTO inventario(idInventario,manos1,manos2,pie,"
                  + "cabeza,pecho,accesorio) VALUES (?,-1,-1,-1,-1,-1,-1)");
            stRegistrarInventario.setInt(1, idInventarioMochila);

            // Preparo la consulta para el registro la mochila en la base de
            // datos
            PreparedStatement stRegistrarMochila = connect.prepareStatement(
                  "INSERT INTO mochila(idMochila,item1,item2,item3,item4,item5,"
                + "item6,item7,item8,item9,item10,item11,item12,item13,item14,"
                + "item15,item16,item17,item18,item19,item20)"
                + "VALUES(?,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,"
                + "-1,-1,-1,-1,-1,-1,-1)");
            stRegistrarMochila.setInt(1, idInventarioMochila);

            // Registro inventario y mochila
            stRegistrarInventario.execute();
            stRegistrarMochila.execute();

            // Le asigno el inventario y la mochila al personaje
            PreparedStatement stAsignarPersonaje = connect
                 .prepareStatement("UPDATE personaje SET idInventario=?,"
                 + "idMochila=? WHERE idPersonaje=?");
            stAsignarPersonaje.setInt(PARAM1, idInventarioMochila);
            stAsignarPersonaje.setInt(PARAM2, idInventarioMochila);
            stAsignarPersonaje.setInt(PARAM3, idInventarioMochila);
            stAsignarPersonaje.execute();

            Servidor.log.append("Se ha registrado el inventario de "
            + idInventarioMochila + System.lineSeparator());
            return true;

         } catch (SQLException e) {
             Servidor.log.append("Error al registrar el inventario de "
         + idInventarioMochila + System.lineSeparator());
           return false;
        }
    }

    /**
     * Método que se encarga de loguear al usuario.
     * @param user PaqueteUsuario con los datos del usuario a loguear
     * @return boolean Resultado del log
     */
    	public boolean loguearUsuario(final PaqueteUsuario user) {
    		//Preparo sesion de hibernate
     		Configuration cfg = new Configuration();
     		cfg.configure("hibernate.cfg.xml");
     		SessionFactory factory = cfg.buildSessionFactory();
     		Session session = factory.openSession();
     		
     		HibernateUtil.openThreadSession(session);   	   		
     		
     		//Preparo el criteria
     		CriteriaBuilder cBuilder = session.getCriteriaBuilder();
     		CriteriaQuery<PaqueteUsuario> cQuery = cBuilder.createQuery(PaqueteUsuario.class);
     		Root<PaqueteUsuario> root = cQuery.from(PaqueteUsuario.class);
     		
     		
     		//Ejecuto la query buscando usuarios con ese nombre
     		cQuery.select(root).where(cBuilder.equal(root.get("username"), user.getUsername()));
     		
     		//Si no existen usuarios con ese nombre
     		int passDB = Integer.parseInt(session.createQuery(cQuery).getResultList().remove(0).getPassword());
            int passIngresada = Integer.parseInt(user.getPassword());
           
            //Si no existen usuarios con ese nombre
            if(!session.createQuery(cQuery).getResultList().isEmpty() && 
              (passIngresada == passDB)) {
             
             Servidor.log.append("El usuario " + user.getUsername()
                     + " ha iniciado sesión." + System.lineSeparator());
             HibernateUtil.closeThreadSession(session, factory);
                     return true;
            }     		
     		return false;
    	}


     /**
      * Método que acualiza los datos del personaje en la base de datos.
      * @param paquetePersonaje PaquetePersonaje con los datos del personaje
      */
     @SuppressWarnings("deprecation")
	public void actualizarPersonaje(final PaquetePersonaje paquetePersonaje) {
        /* THE OLD GOT*/
          try {
            int i = 2;
            int j = 1;
            PreparedStatement stActualizarPersonaje = connect
                  .prepareStatement("UPDATE personaje SET fuerza=?,"
                  + "destreza=?, inteligencia=?, saludTope=?, energiaTope=?,"
                  + "experiencia=?, nivel=?, puntosSkills=?, fuerzaSkill=?,"
                  + "inteligenciaSkill=?, destrezaSkill=? "
                  + "WHERE idPersonaje=?");

            stActualizarPersonaje.setInt(PARAM1, paquetePersonaje.getFuerza());
            stActualizarPersonaje.setInt(PARAM2,
                  paquetePersonaje.getDestreza());
            stActualizarPersonaje.setInt(PARAM3,
                  paquetePersonaje.getInteligencia());
            stActualizarPersonaje.setInt(PARAM4,
                  paquetePersonaje.getSaludTope());
            stActualizarPersonaje.setInt(PARAM5,
                  paquetePersonaje.getEnergiaTope());
            stActualizarPersonaje.setInt(PARAM6,
                  paquetePersonaje.getExperiencia());
            stActualizarPersonaje.setInt(PARAM7, paquetePersonaje.getNivel());
            stActualizarPersonaje.setInt(PARAM8,
                  paquetePersonaje.getPuntosSkillsDisponibles());
            stActualizarPersonaje.setInt(PARAM9,
            paquetePersonaje.getFuerzaSkill());
            stActualizarPersonaje.setInt(PARAM10,
                 paquetePersonaje.getInteligenciaSkill());
            stActualizarPersonaje.setInt(PARAM11,
                 paquetePersonaje.getDestrezaSkill());
            stActualizarPersonaje.setInt(PARAM12, paquetePersonaje.getId());
            stActualizarPersonaje.executeUpdate();


            PreparedStatement stDameItemsID
            = connect.prepareStatement("SELECT * "
                   + "FROM mochila WHERE idMochila = ?");
            stDameItemsID.setInt(1, paquetePersonaje.getId());
            ResultSet resultadoItemsID = stDameItemsID.executeQuery();
            
            PreparedStatement stDatosItem = connect.prepareStatement(
                  "SELECT * FROM item WHERE idItem = ?");
            ResultSet resultadoDatoItem = null;
            paquetePersonaje.eliminarItems();

            while (j <= CANTITEMS) {
                  if (resultadoItemsID.getInt(i) != -1) {
                    stDatosItem.setInt(1, resultadoItemsID.getInt(i));
                    resultadoDatoItem = stDatosItem.executeQuery();

                    paquetePersonaje.anadirItem(resultadoDatoItem.getInt(
                            "idItem"), resultadoDatoItem.getString("nombre"),
                            resultadoDatoItem.getInt("wereable"),
                            resultadoDatoItem.getInt("bonusSalud"),
                            resultadoDatoItem.getInt("bonusEnergia"),
                            resultadoDatoItem.getInt("bonusFuerza"),
                            resultadoDatoItem.getInt("bonusDestreza"),
                            resultadoDatoItem.getInt("bonusInteligencia"),
                            resultadoDatoItem.getString("foto"),
                            resultadoDatoItem.getString("fotoEquipado"));
                }
                i++;
                j++;
            }
              Servidor.log.append("El personaje " + paquetePersonaje.getNombre()
              + " se ha actualizado con éxito." + System.lineSeparator());
            } catch (SQLException e) {
             Servidor.log.append("Fallo al intentar actualizar el personaje "
            + paquetePersonaje.getNombre()  + System.lineSeparator());
        }

    	/*	INTENTO - accede a las columnas, intente iterar por las propiedades de la clase y no salio
    	 * Configuration cfg = new Configuration();
    		cfg.configure("hibernate.cfg.xml");
    		SessionFactory factory = cfg.buildSessionFactory();
    		Session session = factory.openSession();

    		Transaction tx = null;

    		try {
    			tx = session.beginTransaction();
    			Query query = session
    					.createQuery("UPDATE personaje SET fuerza=:fuerza,"
    			                  + "destreza=:destreza, inteligencia=:inteligencia, saludTope=:saludTope, energiaTope=:energiaTope,"
    			                  + "experiencia=?, nivel=?, puntosSkills=?, fuerzaSkill=?,"
    			                  + "inteligenciaSkill=:inteligenciaSkill, destrezaSkill=:destrezaSkill "
    			                  + "WHERE idPersonaje=:idPersonaje");
    			// Seteo parametros items
    			
    			query.setParameter("fuerza",paquetePersonaje.getFuerza());
    			query.setParameter("destreza",paquetePersonaje.getDestreza());
    			query.setParameter("inteligencia",paquetePersonaje.getInteligencia());
    			query.setParameter("saludTope",paquetePersonaje.getSaludTope());
    			query.setParameter("energiaTope",paquetePersonaje.getEnergiaTope());
    			query.setParameter("experiencia",paquetePersonaje.getExperiencia());
    			query.setParameter("nivel",paquetePersonaje.getNivel());
    			query.setParameter("puntosSkills",paquetePersonaje.getPuntosSkillsDisponibles());
    			query.setParameter("fuerzaSkill",paquetePersonaje.getFuerzaSkill());
    			query.setParameter("inteligenciaSkill",paquetePersonaje.getInteligenciaSkill());
    			query.setParameter("destrezaSkill",paquetePersonaje.getDestrezaSkill());
    			query.setParameter("idPersonaje",paquetePersonaje.getId());
    			    		   			
    			int result = query.executeUpdate();

    			
    			Session sessionMochila = factory.openSession();    			
    			Query queryMochila = sessionMochila.createQuery("FROM mochila WHERE idMochila =:idMochila");
    			queryMochila.setParameter("idMochila",paquetePersonaje.getId());
    	           
    			
    			
    			//List<mochila> resultadoItemsID = queryMochila.list();   		
    			mochila resultadoItemsID = (mochila)queryMochila.getSingleResult();
    			Session sessionItem = factory.openSession();    			
    			Query queryitem = sessionItem.createQuery("FROM item WHERE idItem =:idItem");

    			int i = 2;
    	        int j = 1;
	            
	            List<Item>resultadoDatoItem = null;
	            paquetePersonaje.eliminarItems();
	           
    	        while (j <= CANTITEMS) {
    	        	if (resultadoItemsID.getItem1() != -1) {
    	                  //if (resultadoItemsID.getInt(i) != -1) {
    	        		
    	        		queryitem.setParameter("idItem", resultadoItemsID.getItem1());
    	                    resultadoDatoItem = queryitem.executeQuery();

    	                    paquetePersonaje.anadirItem(resultadoDatoItem.getInt(
    	                            "idItem"), resultadoDatoItem.getString("nombre"),
    	                            resultadoDatoItem.getInt("wereable"),
    	                            resultadoDatoItem.getInt("bonusSalud"),
    	                            resultadoDatoItem.getInt("bonusEnergia"),
    	                            resultadoDatoItem.getInt("bonusFuerza"),
    	                            resultadoDatoItem.getInt("bonusDestreza"),
    	                            resultadoDatoItem.getInt("bonusInteligencia"),
    	                            resultadoDatoItem.getString("foto"),
    	                            resultadoDatoItem.getString("fotoEquipado"));
    	                }
    	                i++;
    	                j++;
    	            }
    			
    			
    			tx.commit();
    		}

    		catch (Exception e) {
    			if (tx != null)
    				tx.rollback();
    			Servidor.log.append("Fallo al intentar actualizar personaje" + System.lineSeparator());
    			Servidor.log.append(e.getMessage() + System.lineSeparator());
    		} finally {
    			session.close();
    		}
   
    		*/
    		
    }

    /**
     * Método que se encarga de traer el personaje asociado al usuario
     * a loguear.
     * @param user PaqueteUsuario con los datos correspondientes.
     * @return PaquetePersonaje con los datos del personaje asociado al usuario.
     * @throws IOException En caso de no poder obtener el personaje.
     */
    public PaquetePersonaje getPersonaje(
            final PaqueteUsuario user) throws IOException {
        ResultSet result = null;
        ResultSet resultadoItemsID = null;
        ResultSet resultadoDatoItem = null;
        int i = 2;
        int j = 0;
        try {
            // Selecciono el personaje de ese usuario
            PreparedStatement st = connect.prepareStatement(
                   "SELECT * FROM registro WHERE usuario = ?");
            st.setString(1, user.getUsername());
            result = st.executeQuery();

            // Obtengo el id
            int idPersonaje = result.getInt("idPersonaje");

            // Selecciono los datos del personaje
            PreparedStatement stSeleccionarPersonaje = connect
                  .prepareStatement("SELECT * FROM personaje "
                         + "WHERE idPersonaje = ?");
            stSeleccionarPersonaje.setInt(1, idPersonaje);
            result = stSeleccionarPersonaje.executeQuery();
            // Traigo los id de los items correspondientes a mi personaje
            PreparedStatement stDameItemsID = connect.prepareStatement(
                   "SELECT * FROM mochila WHERE idMochila = ?");
            stDameItemsID.setInt(1, idPersonaje);
            resultadoItemsID = stDameItemsID.executeQuery();
            // Traigo los datos del item
            PreparedStatement stDatosItem = connect.prepareStatement(
                   "SELECT * FROM item WHERE idItem = ?");

            // Obtengo los atributos del personaje
            PaquetePersonaje personaje = new PaquetePersonaje();
            personaje.setId(idPersonaje);
            personaje.setRaza(result.getString("raza"));
            personaje.setCasta(result.getString("casta"));
            personaje.setFuerza(result.getInt("fuerza"));
            personaje.setInteligencia(result.getInt("inteligencia"));
            personaje.setDestreza(result.getInt("destreza"));
            personaje.setEnergiaTope(result.getInt("energiaTope"));
            personaje.setSaludTope(result.getInt("saludTope"));
            personaje.setNombre(result.getString("nombre"));
            personaje.setExperiencia(result.getInt("experiencia"));
            personaje.setNivel(result.getInt("nivel"));
            personaje.setPuntosSkillsDisponibles(result.getInt("puntosSkills"));
            personaje.setFuerzaSkill(result.getInt("fuerzaSkill"));
            personaje.setDestrezaSkill(result.getInt("destrezaSkill"));
            personaje.setInteligenciaSkill(result.getInt("inteligenciaSkill"));

            while (j <= CANTITEMS) {
                if (resultadoItemsID.getInt(i) != -1) {
                    stDatosItem.setInt(1, resultadoItemsID.getInt(i));
                    resultadoDatoItem = stDatosItem.executeQuery();
                    personaje.anadirItem(resultadoDatoItem.getInt("idItem"),
                          resultadoDatoItem.getString("nombre"),
                          resultadoDatoItem.getInt("wereable"),
                          resultadoDatoItem.getInt("bonusSalud"),
                          resultadoDatoItem.getInt("bonusEnergia"),
                          resultadoDatoItem.getInt("bonusFuerza"),
                          resultadoDatoItem.getInt("bonusDestreza"),
                          resultadoDatoItem.getInt("bonusInteligencia"),
                          resultadoDatoItem.getString("foto"),
                          resultadoDatoItem.getString("fotoEquipado"));
                 }
                i++;
                j++;
           }


            // Devuelvo el paquete personaje con sus datos
            return personaje;

        } catch (SQLException ex) {
              Servidor.log.append("Fallo al intentar recuperar el personaje "
                   + user.getUsername() + System.lineSeparator());
        Servidor.log.append(ex.getMessage() + System.lineSeparator());
        }

        return new PaquetePersonaje();
    }

    /**
     * Método encargado de traer los datos del usuario de
     * la base de datos.
     * @param usuario Nombre de usuario
     * @return PaqueteUsuario con los datos del usuario
     */
	public PaqueteUsuario getUsuario(final String usuario) {//HBN DONE!
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from registro where usuario = :userName ");
			query.setParameter("userName", usuario);
			List<PaqueteUsuario> userList = query.list();
			tx.commit();

			if (userList != null && !userList.isEmpty()) {
				return userList.get(0);
			}
		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar recuperar el usuario " + usuario + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
		}

		return new PaqueteUsuario();
	}

     /**
      * Método encargado de actualizar el inventario del personaje.
      * @param paquetePersonaje datos del personaje a actualizar.
      */
     public void actualizarInventario(final PaquetePersonaje paquetePersonaje) {//HBN DONE!
       /* int i = 0;
        PreparedStatement stActualizarMochila;
        try {
            stActualizarMochila = connect.prepareStatement(
                    "UPDATE mochila SET item1=? ,item2=? ,item3=? ,item4=?"
                   + ",item5=? ,item6=? ,item7=? ,item8=? ,item9=? "
                   + ",item10=? ,item11=? ,item12=? ,item13=? ,item14=?"
                   + ",item15=?,item16=? ,item17=? ,item18=? ,item19=?"
                   + ",item20=? WHERE idMochila=?");
           while (i < paquetePersonaje.getCantItems()) {
               stActualizarMochila.setInt(i + 1, paquetePersonaje.getItemID(i));
               i++;
           }
           for (int j = paquetePersonaje.getCantItems();
                   j < CANTITEMSMAXMOCHILA; j++) {
            stActualizarMochila.setInt(j + 1, -1);
            }
            stActualizarMochila.setInt(PARAM21, paquetePersonaje.getId());
            stActualizarMochila.executeUpdate();

         } catch (SQLException e) {
         }*/
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE mochila " + "SET item1 = :it1 ,item2 = :it2 ,item3 = :it3 ,item4 = :it4 ,"
							+ "item5 = :it5 ,item6 = :it6 ,item7 = :it7 ,item8 = :it8 ,"
							+ "item9 = :it9 ,item10 = :it10 ,item11 = :it11 ,item12 = :it12 ,"
							+ "item13 = :it13 ,item14 = :it14 ,item15 = :it15 ,item16 = :it16 ,"
							+ "item17 = :it17 ,item18 = :it18 ,item19 = :it19 ,item20 = :it20"
							+ "WHERE idMochila = :idMochila");
			// Seteo parametros items
			for (int i = 0; i < paquetePersonaje.getCantItems(); i++) {
				query.setParameter("item" + (i + 1), paquetePersonaje.getItemID(i));
			}
			// seteo el resto vacio
			for (int j = paquetePersonaje.getCantItems(); j < CANTITEMSMAXMOCHILA; j++) {
				query.setParameter("item" + (j + 1), -1);
			}
			query.setParameter("idMochila", paquetePersonaje.getId());
			int result = query.executeUpdate();

			tx.commit();
		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar actualizar inventario" + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
		}

    }

    /**
     * Método que actualiza el inventario del personaje.
     * @param idPersonaje al que se le desea actualizar el inventario.
     */
     public void actualizarInventario(final int idPersonaje) { //HBN DONE!
       /* int i = 0;
        PaquetePersonaje paquetePersonaje = Servidor
             .getPersonajesConectados().get(idPersonaje);
        PreparedStatement stActualizarMochila;
        try {
            stActualizarMochila = connect.prepareStatement(
                 "UPDATE mochila SET item1=? ,item2=? ,item3=?"
                 + ",item4=? ,item5=? ,item6=? ,item7=? ,item8=? ,item9=? "
                 + ",item10=? ,item11=? ,item12=? ,item13=? ,item14=?"
                 + ",item15=? ,item16=? ,item17=? ,item18=? ,item19=?"
                 + ",item20=? WHERE idMochila=?");
        while (i < paquetePersonaje.getCantItems()) {
             stActualizarMochila.setInt(i + 1, paquetePersonaje.getItemID(i));
             i++;
             }
          if (paquetePersonaje.getCantItems() < CANTITEMS) {
                int itemGanado = new Random().nextInt(
                      CANTITEMS + CANTITEMSMAXMOCHILA);
                itemGanado += 1;
                stActualizarMochila.setInt(
                      paquetePersonaje.getCantItems() + 1, itemGanado);
                for (int j = paquetePersonaje.getCantItems() + 2;
                      j < CANTITEMSMAXMOCHILA; j++) {
                   stActualizarMochila.setInt(j, -1);
                }
            } else {
              for (int j = paquetePersonaje.getCantItems() + 1;
                      j < CANTITEMSMAXMOCHILA; j++) {
                  stActualizarMochila.setInt(j, -1);
              }
            }
            stActualizarMochila.setInt(PARAM21, paquetePersonaje.getId());
            stActualizarMochila.executeUpdate();

        } catch (SQLException e) {
             Servidor.log.append("Falló al intentar actualizar"
                  + "inventario de" + idPersonaje + "\n");
         }*/
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		PaquetePersonaje paquetePersonaje = Servidor.getPersonajesConectados().get(idPersonaje);
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE mochila " + "SET item1 = :it1 ,item2 = :it2 ,item3 = :it3 ,item4 = :it4 ,"
							+ "item5 = :it5 ,item6 = :it6 ,item7 = :it7 ,item8 = :it8 ,"
							+ "item9 = :it9 ,item10 = :it10 ,item11 = :it11 ,item12 = :it12 ,"
							+ "item13 = :it13 ,item14 = :it14 ,item15 = :it15 ,item16 = :it16 ,"
							+ "item17 = :it17 ,item18 = :it18 ,item19 = :it19 ,item20 = :it20"
							+ "WHERE idMochila = :idMochila");

			// Seteo parametros items
			for (int i = 0; i < paquetePersonaje.getCantItems(); i++) {
				query.setParameter("item" + (i + 1), paquetePersonaje.getItemID(i));
			}

			if (paquetePersonaje.getCantItems() < CANTITEMS) {
				int itemGanado = new Random().nextInt(CANTITEMS + CANTITEMSMAXMOCHILA);
				itemGanado += 1;
				query.setParameter("item" + paquetePersonaje.getCantItems() + 1, itemGanado);

				for (int j = paquetePersonaje.getCantItems() + 2; j < CANTITEMSMAXMOCHILA; j++) {
					query.setParameter("item" + j, -1);
				}
			} else {
				for (int j = paquetePersonaje.getCantItems() + 1; j < CANTITEMSMAXMOCHILA; j++) {
					query.setParameter("item" + j, -1);
				}
			}

			// seteo el resto vacio
			for (int j = paquetePersonaje.getCantItems(); j < CANTITEMSMAXMOCHILA; j++) {
				query.setParameter("item" + (j + 1), -1);
			}
			query.setParameter("idMochila", paquetePersonaje.getId());
			int result = query.executeUpdate();

			tx.commit();
		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar actualizar inventario" + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
		}

    }

     /**
      * Método encargado de actualizar el nivel del personaje.
      * @param paquetePersonaje datos del personaje a actualizar.
      */
	public void actualizarPersonajeSubioNivel(final PaquetePersonaje paquetePersonaje) {

		/*
		 * try { PreparedStatement stActualizarPersonaje = connect
		 * .prepareStatement("UPDATE personaje SET fuerza=?," +
		 * "destreza=?, inteligencia=?, saludTope=?, energiaTope=?," +
		 * "experiencia=?, nivel=?, puntosSkills=?, fuerzaSkill=?," +
		 * "inteligenciaSkill=?, destrezaSkill=?" + " WHERE idPersonaje=?");
		 * 
		 * stActualizarPersonaje.setInt(PARAM1, paquetePersonaje.getFuerza());
		 * stActualizarPersonaje.setInt(PARAM2, paquetePersonaje.getDestreza());
		 * stActualizarPersonaje.setInt(PARAM3, paquetePersonaje.getInteligencia());
		 * stActualizarPersonaje.setInt(PARAM4, paquetePersonaje.getSaludTope());
		 * stActualizarPersonaje.setInt(PARAM5, paquetePersonaje.getEnergiaTope());
		 * stActualizarPersonaje.setInt(PARAM6, paquetePersonaje.getExperiencia());
		 * stActualizarPersonaje.setInt(PARAM7, paquetePersonaje.getNivel());
		 * stActualizarPersonaje.setInt(PARAM8,
		 * paquetePersonaje.getPuntosSkillsDisponibles());
		 * stActualizarPersonaje.setInt(PARAM9, paquetePersonaje.getFuerzaSkill());
		 * stActualizarPersonaje.setInt(PARAM10,
		 * paquetePersonaje.getInteligenciaSkill());
		 * stActualizarPersonaje.setInt(PARAM11, paquetePersonaje.getDestrezaSkill());
		 * stActualizarPersonaje.setInt(PARAM12, paquetePersonaje.getId());
		 * 
		 * stActualizarPersonaje.executeUpdate();
		 * 
		 * Servidor.log.append("El personaje " + paquetePersonaje.getNombre() +
		 * " se ha actualizado con éxito." + System.lineSeparator()); } catch
		 * (SQLException e) {
		 * Servidor.log.append("Fallo al intentar actualizar el personaje " +
		 * paquetePersonaje.getNombre() + System.lineSeparator()); }
		 */
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("UPDATE personaje SET fuerza =:fuerza,"
					+ "destreza =:destreza, inteligencia =:inteligencia, saludTope =:saludTope, energiaTope =:energiaTope,"
					+ "experiencia =:experiencia, nivel=:nivel, puntosSkills=:puntosSkill, fuerzaSkill=:fuerzaSkill"
					+ "inteligenciaSkill=:inteligenciaSkill, destrezaSkill=:destrezaSkill"
					+ " WHERE idPersonaje=:idPersonaje");

			// Seteo parametros items
			query.setParameter("fuerza", paquetePersonaje.getFuerza());
			query.setParameter("destreza", paquetePersonaje.getDestreza());
			query.setParameter("inteligencia", paquetePersonaje.getInteligencia());
			query.setParameter("saludTope", paquetePersonaje.getSaludTope());
			query.setParameter("energiaTope", paquetePersonaje.getEnergiaTope());
			query.setParameter("experiencia", paquetePersonaje.getExperiencia());
			query.setParameter("nivel", paquetePersonaje.getNivel());
			query.setParameter("puntosSkills", paquetePersonaje.getPuntosSkillsDisponibles());
			query.setParameter("fuerzaSkill", paquetePersonaje.getFuerzaSkill());
			query.setParameter("inteligenciaSkill", paquetePersonaje.getInteligenciaSkill());
			query.setParameter("destrezaSkill", paquetePersonaje.getDestrezaSkill());
			query.setParameter("idPersonaje", paquetePersonaje.getId());

			int result = query.executeUpdate();

			tx.commit();
		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar actualizar el nivel del personaje" + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
		}
    	 
    }
}
