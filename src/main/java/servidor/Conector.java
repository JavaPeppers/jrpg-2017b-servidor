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
	
	/*TODO:
	 * 1 - ver los atributos que agregamos asi nomas, idMochila, etc
	 * 2 - Sumar los 3 metodos que faltan
	 * 3 - eliminar jdbc
	 * */
	
	
    /** url de la base de datos. **/
    private String url = "primeraBase.bd";

    /** Variable que se utilizará para la conexión. **/
    private Connection connect;

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
		// Preparo sesion de hibernate
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		HibernateUtil.openThreadSession(session);

		// Preparo el criteria
		CriteriaBuilder cBuilder = session.getCriteriaBuilder();
		CriteriaQuery<PaqueteUsuario> cQuery = cBuilder.createQuery(PaqueteUsuario.class);
		Root<PaqueteUsuario> root = cQuery.from(PaqueteUsuario.class);

		// Ejecuto la query buscando usuarios con ese nombre
		cQuery.select(root).where(cBuilder.equal(root.get("username"), user.getUsername()));

		// Si no existen usuarios con ese nombre
		if (session.createQuery(cQuery).getResultList().isEmpty()) {
			// Registro el usuario
			Transaction transaccion = session.beginTransaction();
			try {
				session.save(user);
				transaccion.commit();

			} catch (HibernateException e) {
				// Si falló, hago un rollback de la transaccion, cierro sesion, escribo el log y
				// me voy
				if (transaccion != null)
					transaccion.rollback();
				e.printStackTrace();

				HibernateUtil.closeThreadSession(session, factory);

				Servidor.log
						.append("Eror al intentar registrar el usuario " + user.getUsername() + System.lineSeparator());
				return false;
			}
		} else {
			// Si ya existe un usuario con ese nombre, cierro sesion, escribo el log y me
			// voy
			HibernateUtil.closeThreadSession(session, factory);

			Servidor.log
					.append("El usuario " + user.getUsername() + " ya se encuentra en uso." + System.lineSeparator());
			return false;
		}

		HibernateUtil.closeThreadSession(session, factory);

		Servidor.log.append("El usuario " + user.getUsername() + " se ha registrado." + System.lineSeparator());
		return true;
	}

	public boolean registrarPersonaje(PaquetePersonaje pj, PaqueteUsuario user) {

		// Preparo sesion de hibernate
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		// HibernateUtil.openThreadSession(session);

		// Seteo stats del personaje
		pj.setAlianza(-1);
		pj.setExperiencia(0);
		pj.setIdInventario(pj.getId());
		pj.setIdMochila(pj.getId());
		pj.setNivel(1);

		Transaction transaccion = session.beginTransaction();
		try {
			pj.setId((Integer) session.save(pj));

			transaccion.commit();
			System.out.println(pj.getId());
			user.setIdPj(pj.getId());

			session.close();
			factory.close();
			actualizarUsuario(user);

			// Salida por true o false dependiendo de como
			if (this.registrarInventarioMochila(pj.getId())) {
				Servidor.log.append("El usuario " + user.getUsername() + " ha creado el personaje " + pj.getId()
						+ System.lineSeparator());

				HibernateUtil.closeThreadSession(session, factory);

				return true;
			} else {

				HibernateUtil.closeThreadSession(session, factory);


				Servidor.log.append("Error al registrar la mochila y el inventario del usuario " + user.getUsername()
						+ " con el personaje" + pj.getId() + System.lineSeparator());
				return false;
			}
		} catch (HibernateException e) {
			// Si falló, hago un rollback de la transaccion, cierro sesion, escribo el log y
			// me voy
			if (transaccion != null)
				transaccion.rollback();
			e.printStackTrace();

			HibernateUtil.closeThreadSession(session, factory);

			Servidor.log.append("Error al intentar crear el personaje " + pj.getNombre() + System.lineSeparator());
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
	public boolean registrarInventarioMochila(final int idInventarioMochila) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			// Preparo la consulta para el registro el inventario en la base de
			// datos
			Inv inventario = new Inv(idInventarioMochila, -1, -1, -1, -1, -1, -1);
			session.save(inventario);

			// Registro inventario y mochila
			Session sessionMochila = factory.openSession();
			Mochila bag = new Mochila(idInventarioMochila, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1);

			session.save(bag);

			Query query = session.createQuery("UPDATE PaquetePersonaje SET idInventario= :idInventario,"
					+ " idMochila= :idMochila WHERE idPersonaje= :idPersonaje");

			query.setParameter("idInventario", idInventarioMochila);
			query.setParameter("idMochila", idInventarioMochila);
			query.setParameter("idPersonaje", idInventarioMochila);
			int result = query.executeUpdate();

			Servidor.log.append("Se ha registrado el inventario de " + idInventarioMochila + System.lineSeparator());

			tx.commit();
			return true;

		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar registrar el inventario Mochila" + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * Método que se encarga de loguear al usuario.
	 * 
	 * @param user
	 *            PaqueteUsuario con los datos del usuario a loguear
	 * @return boolean Resultado del log
	 */
	public boolean loguearUsuario(final PaqueteUsuario user) {
		// Preparo sesion de hibernate
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();		

		// Preparo el criteria
		CriteriaBuilder cBuilder = session.getCriteriaBuilder();
		CriteriaQuery<PaqueteUsuario> cQuery = cBuilder.createQuery(PaqueteUsuario.class);
		Root<PaqueteUsuario> root = cQuery.from(PaqueteUsuario.class);

		// Ejecuto la query buscando usuarios con ese nombre
		cQuery.select(root).where(cBuilder.equal(root.get("username"), user.getUsername()));

		// Si no existen usuarios con ese nombre
		int passDB = Integer.parseInt(session.createQuery(cQuery).getResultList().remove(0).getPassword());
		int passIngresada = Integer.parseInt(user.getPassword());

		// Si no existen usuarios con ese nombre
		if (!session.createQuery(cQuery).getResultList().isEmpty() && (passIngresada == passDB)) {

			Servidor.log.append("El usuario " + user.getUsername() + " ha iniciado sesión." + System.lineSeparator());
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
		/* THE OLD GOT */
		/*
		 * try { int i = 2; int j = 1; PreparedStatement stActualizarPersonaje = connect
		 * .prepareStatement("UPDATE personaje SET fuerza=?," +
		 * "destreza=?, inteligencia=?, saludTope=?, energiaTope=?," +
		 * "experiencia=?, nivel=?, puntosSkills=?, fuerzaSkill=?," +
		 * "inteligenciaSkill=?, destrezaSkill=? " + "WHERE idPersonaje=?");
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
		 * stActualizarPersonaje.executeUpdate();
		 * 
		 * 
		 * PreparedStatement stDameItemsID = connect.prepareStatement("SELECT * " +
		 * "FROM mochila WHERE idMochila = ?"); stDameItemsID.setInt(1,
		 * paquetePersonaje.getId()); ResultSet resultadoItemsID =
		 * stDameItemsID.executeQuery();
		 * 
		 * PreparedStatement stDatosItem = connect.prepareStatement(
		 * "SELECT * FROM item WHERE idItem = ?"); ResultSet resultadoDatoItem = null;
		 * paquetePersonaje.eliminarItems();
		 * 
		 * while (j <= CANTITEMS) { if (resultadoItemsID.getInt(i) != -1) {
		 * stDatosItem.setInt(1, resultadoItemsID.getInt(i)); resultadoDatoItem =
		 * stDatosItem.executeQuery();
		 * 
		 * paquetePersonaje.anadirItem(resultadoDatoItem.getInt( "idItem"),
		 * resultadoDatoItem.getString("nombre"), resultadoDatoItem.getInt("wereable"),
		 * resultadoDatoItem.getInt("bonusSalud"),
		 * resultadoDatoItem.getInt("bonusEnergia"),
		 * resultadoDatoItem.getInt("bonusFuerza"),
		 * resultadoDatoItem.getInt("bonusDestreza"),
		 * resultadoDatoItem.getInt("bonusInteligencia"),
		 * resultadoDatoItem.getString("foto"),
		 * resultadoDatoItem.getString("fotoEquipado")); } i++; j++; }
		 * Servidor.log.append("El personaje " + paquetePersonaje.getNombre() +
		 * " se ha actualizado con éxito." + System.lineSeparator()); } catch
		 * (SQLException e) {
		 * Servidor.log.append("Fallo al intentar actualizar el personaje " +
		 * paquetePersonaje.getNombre() + System.lineSeparator()); }
		 */
		// INTENTO - accede a las columnas, intente iterar por las propiedades de la
		// clase y no salio
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		//Session sessionMochila = factory.openSession();
		
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("UPDATE PaquetePersonaje SET fuerza=:fuerza,"
					+ "destreza=:destreza, inteligencia=:inteligencia, saludTope=:saludTope, energiaTope=:energiaTope,"
					+ "experiencia= :experiencia, nivel= :nivel, puntosSkills= :puntosSkills, fuerzaSkill= :fuerzaSkill,"
					+ "inteligenciaSkill=:inteligenciaSkill, destrezaSkill=:destrezaSkill "
					+ "WHERE idPersonaje=:idPersonaje");

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

			
			Query queryMochila = session.createQuery("FROM Mochila WHERE idMochila = :idMochila");
			queryMochila.setParameter("idMochila", paquetePersonaje.getId());
			//Mochila resultadoItemsID = (Mochila) queryMochila.getSingleResult();
			List<Mochila> resultadoItemsIDList = queryMochila.list();
			//Session sessionItem = factory.openSession();
			Query queryitem;

			int i = 2;
			int j = 1;
			Mochila resultadoItemsID;
			//dbPersonaje.eliminarItems();
			if (resultadoItemsIDList != null && !resultadoItemsIDList.isEmpty()) {

				resultadoItemsID = resultadoItemsIDList.get(0);

				while (j <= CANTITEMS) {
					if (resultadoItemsID.getByItemId(i) != -1) {// si hay algo

						queryitem = session.createQuery("FROM ItemHb WHERE idItem = :idItem");
						queryitem.setParameter("idItem", resultadoItemsID.getByItemId(i));

						ItemHb resultadoDatoItem = (ItemHb) queryitem.getSingleResult();

						paquetePersonaje.anadirItem(resultadoDatoItem.getIdItem(), resultadoDatoItem.getNombre(),
								resultadoDatoItem.getWereable(), resultadoDatoItem.getBonusSalud(),
								resultadoDatoItem.getBonusEnergia(), resultadoDatoItem.getBonusFuerza(),
								resultadoDatoItem.getBonusDestreza(), resultadoDatoItem.getBonusInteligencia(),
								resultadoDatoItem.getFoto(), resultadoDatoItem.getFotoEquipado());
					}
					i++;
					j++;
				}
			tx.commit();			 
		  }
		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar actualizar personaje" + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
			//sessionMochila.close();
		}

	}

    /**
     * Método que se encarga de traer el personaje asociado al usuario
     * a loguear.
     * @param user PaqueteUsuario con los datos correspondientes.
     * @return PaquetePersonaje con los datos del personaje asociado al usuario.
     * @throws IOException En caso de no poder obtener el personaje.
     */
	@SuppressWarnings("finally")
	public PaquetePersonaje getPersonaje(final PaqueteUsuario user) throws IOException {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			Query queryUsuario = session.createQuery("from PaqueteUsuario where usuario = :userName ");
			queryUsuario.setParameter("userName", user.getUsername());
			PaqueteUsuario dbUser = (PaqueteUsuario) queryUsuario.getSingleResult();

			int idPersonaje = dbUser.getIdPj();

			// Selecciono los datos del personaje
			Query queryPersonaje = session.createQuery("from PaquetePersonaje where idPersonaje = :idPersonaje ");
			queryPersonaje.setParameter("idPersonaje", idPersonaje);
			PaquetePersonaje dbPersonaje = (PaquetePersonaje) queryPersonaje.getSingleResult();

			// Traigo los id de los items correspondientes a mi personaje
			//Session sessionMochila = factory.openSession();
			Query queryMochila = session.createQuery("FROM Mochila WHERE idMochila = :idMochila");
			queryMochila.setParameter("idMochila", dbPersonaje.getId());
			//Mochila resultadoItemsID = (Mochila) queryMochila.getSingleResult();
			List<Mochila> resultadoItemsIDList = queryMochila.list();
			//Session sessionItem = factory.openSession();
			Query queryitem;

			int i = 2;
			int j = 1;
			Mochila resultadoItemsID;
			dbPersonaje.eliminarItems();
			if (resultadoItemsIDList != null && !resultadoItemsIDList.isEmpty()) {

				resultadoItemsID = resultadoItemsIDList.get(0);

				while (j <= CANTITEMS) {
					if (resultadoItemsID.getByItemId(i) != -1) {// si hay algo

						queryitem = session.createQuery("FROM ItemHb WHERE idItem = :idItem");
						queryitem.setParameter("idItem", resultadoItemsID.getByItemId(i));

						ItemHb resultadoDatoItem = (ItemHb) queryitem.getSingleResult();

						dbPersonaje.anadirItem(resultadoDatoItem.getIdItem(), resultadoDatoItem.getNombre(),
								resultadoDatoItem.getWereable(), resultadoDatoItem.getBonusSalud(),
								resultadoDatoItem.getBonusEnergia(), resultadoDatoItem.getBonusFuerza(),
								resultadoDatoItem.getBonusDestreza(), resultadoDatoItem.getBonusInteligencia(),
								resultadoDatoItem.getFoto(), resultadoDatoItem.getFotoEquipado());
					}
					i++;
					j++;
				}
			tx.commit();
			return dbPersonaje; 
		  }
		}

		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			Servidor.log.append("Fallo al intentar obtener el personaje" + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();			
		}
		return new PaquetePersonaje();
		/*
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
	                   "SELECT * FROM Mochila WHERE idMochila = ?");
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

	        return new PaquetePersonaje();*/
	}

	/**
	 * Método encargado de traer los datos del usuario de la base de datos.
	 * 
	 * @param usuario
	 *            Nombre de usuario
	 * @return PaqueteUsuario con los datos del usuario
	 */
	public PaqueteUsuario getUsuario(final String usuario) {// HBN DONE!
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from PaqueteUsuario where usuario = :userName ");
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
	 * 
	 * @param paquetePersonaje
	 *            datos del personaje a actualizar.
	 */
	public void actualizarInventario(final PaquetePersonaje paquetePersonaje) {// HBN DONE!

		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE Mochila " + "SET item1 = :it1 ,item2 = :it2 ,item3 = :it3 ,item4 = :it4 ,"
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
	 * 
	 * @param idPersonaje
	 *            al que se le desea actualizar el inventario.
	 */
	public void actualizarInventario(final int idPersonaje) { // HBN DONE!

		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		PaquetePersonaje paquetePersonaje = Servidor.getPersonajesConectados().get(idPersonaje);
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE Mochila " + "SET item1 = :it1 ,item2 = :it2 ,item3 = :it3 ,item4 = :it4 ,"
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
	 * 
	 * @param paquetePersonaje
	 *            datos del personaje a actualizar.
	 */
	public void actualizarPersonajeSubioNivel(final PaquetePersonaje paquetePersonaje) {

		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("UPDATE PaquetePersonaje SET fuerza = :fuerza,"
					+ "destreza = :destreza, inteligencia = :inteligencia, saludTope = :saludTope, energiaTope = :energiaTope,"
					+ "experiencia = :experiencia, nivel= :nivel, puntosSkills= :puntosSkill, fuerzaSkill= :fuerzaSkill,"
					+ "inteligenciaSkill= :inteligenciaSkill, destrezaSkill= :destrezaSkill"
					+ " WHERE idPersonaje= :idPersonaje");

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
