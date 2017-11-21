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

	/*
	 * TODO: 1 - ver los atributos que agregamos asi nomas, idMochila, etc 2 - Sumar
	 * los 3 metodos que faltan 3 - eliminar jdbc
	 */

	/** url de la base de datos. **/
	private String url = "primeraBase.bd";

	/** Variable que se utilizará para la conexión. **/
	private Connection connect;

	/** Variable que indica la cant de items disponibles. **/
	private static final int CANTITEMS = 9;

	/** Variable que indica la cant de items máximos en la mochila. **/
	private static final int CANTITEMSMAXMOCHILA = 20;

	private SessionFactory factory;

	public SessionFactory getSessionFactory() {
		return this.factory;
	}

	public void setSessionFactory(SessionFactory factory) {
		this.factory = factory;
	}

	/**
	 * Método que establece la conexión con la base de datos.
	 */
	public void connect() {
		try {
			Servidor.log.append("Estableciendo conexión " + "con la base de datos..." + System.lineSeparator());
			/* connect = DriverManager.getConnection("jdbc:sqlite:" + url); */

			final Configuration cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			this.setSessionFactory(cfg.buildSessionFactory());

			Servidor.log.append("Conexión con la base de datos " + "establecida con éxito." + System.lineSeparator());

		} catch (HibernateException ex) {
			Servidor.log.append("Fallo al intentar establecer la conexión" + "con la base de datos. " + ex.getMessage()
					+ System.lineSeparator());
		}
	}

	/** Método que cierra la conexión con la base de datos. **/
	public void close() {
		try {
			// connect.close();
			this.getSessionFactory().close();
		} catch (HibernateException ex) {
			Servidor.log
					.append("Error al intentar cerrar la conexión" + "con la base de datos." + System.lineSeparator());
			Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Método encargado de registrar el usuario en la base de datos.
	 * 
	 * @param user
	 *            PaqueteUsuario con los datos del usuario a registrar.
	 * @return boolean Resultado del registro.
	 */

	public boolean registrarUsuario(PaqueteUsuario user) {
		// Preparo sesion de hibernate

		Session session = getSessionFactory().openSession();

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

				// HibernateUtil.closeThreadSession(session, factory);
				session.close();

				Servidor.log
						.append("Eror al intentar registrar el usuario " + user.getUsername() + System.lineSeparator());
				return false;
			}
		} else {
			// Si ya existe un usuario con ese nombre, cierro sesion, escribo el log y me
			// voy
			session.close();
			Servidor.log
					.append("El usuario " + user.getUsername() + " ya se encuentra en uso." + System.lineSeparator());
			return false;
		}

		// HibernateUtil.closeThreadSession(session, factory);
		session.close();

		Servidor.log.append("El usuario " + user.getUsername() + " se ha registrado." + System.lineSeparator());
		return true;
	}

	public boolean registrarPersonaje(PaquetePersonaje pj, PaqueteUsuario user) {

		Session session = getSessionFactory().openSession();

		// HibernateUtil.openThreadSession(session);
		Transaction transaccion = session.beginTransaction();
		try {
			// Personaje

			session.save(pj);
			Servidor.log.append(
					"1 - " + user.getUsername() + " ha creado el personaje: " + pj.getId() + System.lineSeparator());
			// update Usuario
			// pj.setId((Integer) session.save(pj));

			user.setIdPj(pj.getId());
			// actualizarUsuario(user);
			session.update(user);
			Servidor.log.append("2 - " + user.getUsername() + "  termino de crear su pj: " + user.getIdPj()
					+ System.lineSeparator());

			// Registro inventario y mochila
			final Inv inventario = new Inv(pj.getId());
			session.save(inventario);

			Servidor.log.append("3 - " + user.getUsername() + "  termino de crear inventario: "
					+ inventario.getidInventario() + System.lineSeparator());

			final Mochila bag = new Mochila(pj.getId());
			session.save(bag);

			Servidor.log.append("4 - " + user.getUsername() + "  termino de crear mochila: " + bag.getIdMochila()
					+ System.lineSeparator());

			Query query = session.createQuery("UPDATE PaquetePersonaje SET idInventario= :idInventario,"
					+ " idMochila= :idMochila WHERE idPersonaje= :idPersonaje");

			pj.setidInventario(inventario.getidInventario());
			pj.setidMochila(bag.getIdMochila());
			session.update(pj);
			session.update(bag);

			Servidor.log.append("5 - " + user.getUsername() + "  updateo su mochila e inventario " + pj.getidMochila()
					+ " inv: " + pj.getidInventario() + System.lineSeparator());

			transaccion.commit();

			Servidor.log.append("El usuario " + user.getUsername() + " ha creado el personaje " + pj.getId()
					+ System.lineSeparator());

			session.close();
			return true;

		} catch (HibernateException e) {
			// Si falló, hago un rollback de la transaccion, cierro sesion, escribo el log y
			// me voy
			if (transaccion != null)
				transaccion.rollback();
			e.printStackTrace();

			session.close();

			Servidor.log.append("Error al registrar la mochila y el inventario del usuario " + user.getUsername()
					+ " con el personaje" + pj.getId() + System.lineSeparator());

			Servidor.log.append("Error al intentar crear el personaje " + pj.getNombre() + System.lineSeparator());
			return false;
		}

	}

	public void actualizarUsuario(PaqueteUsuario user) {
		// Preparo sesion de hibernate
		Session session = getSessionFactory().openSession();

		try {
			// Preparo el criteria
			CriteriaBuilder cBuilder = session.getCriteriaBuilder();
			CriteriaQuery<PaqueteUsuario> cQuery = cBuilder.createQuery(PaqueteUsuario.class);
			Root<PaqueteUsuario> root = cQuery.from(PaqueteUsuario.class);

			// Actualizo el usuario
			// Transaction transaccion = session.beginTransaction();
			session.update(user);
			// transaccion.commit();
		} catch (HibernateException e) {
			Servidor.log.append("Error al actualizar usuario " + user.getUsername() + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
		} finally {
			session.close();
			// HibernateUtil.closeThreadSession(session, factory);
		}

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
		Session session = getSessionFactory().openSession();

		try {
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

				Servidor.log
						.append("El usuario " + user.getUsername() + " ha iniciado sesión." + System.lineSeparator());
				return true;
			}
			return false;

		} catch (HibernateException e) {
			Servidor.log.append("Error al loguear usuario " + user.getUsername() + System.lineSeparator());
			Servidor.log.append(e.getMessage() + System.lineSeparator());
			return false;
		} finally {
			// HibernateUtil.closeThreadSession(session, factory);
			session.close();
		}

	}

	/**
	 * Método que acualiza los datos del personaje en la base de datos.
	 * 
	 * @param paquetePersonaje
	 *            PaquetePersonaje con los datos del personaje
	 */
	@SuppressWarnings("deprecation")
	public void actualizarPersonaje(final PaquetePersonaje paquetePersonaje) {
		Session session = getSessionFactory().openSession();

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
			// Mochila resultadoItemsID = (Mochila) queryMochila.getSingleResult();
			List<Mochila> resultadoItemsIDList = queryMochila.list();
			// Session sessionItem = factory.openSession();
			Query queryitem;

			int i = 2;
			int j = 1;
			Mochila resultadoItemsID;

			if (resultadoItemsIDList != null && !resultadoItemsIDList.isEmpty()) {

				resultadoItemsID = resultadoItemsIDList.get(0);

				while (j <= CANTITEMS) {
					if (resultadoItemsID.getByItemId(i) != -1) {// si hay algo

						queryitem = session.createQuery("FROM ItemHb WHERE idItem = :idItem");
						queryitem.setParameter("idItem", resultadoItemsID.getByItemId(i));

						List<ItemHb> resultadoDatoItemList = queryitem.list();

						ItemHb resultadoDatoItem = resultadoDatoItemList != null ? resultadoDatoItemList.get(0)
								: new ItemHb();

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
		}

	}

	/**
	 * Método que se encarga de traer el personaje asociado al usuario a loguear.
	 * 
	 * @param user
	 *            PaqueteUsuario con los datos correspondientes.
	 * @return PaquetePersonaje con los datos del personaje asociado al usuario.
	 * @throws IOException
	 *             En caso de no poder obtener el personaje.
	 */
	@SuppressWarnings("finally")
	public PaquetePersonaje getPersonaje(final PaqueteUsuario user) throws IOException {
		Session session = getSessionFactory().openSession();
		PaquetePersonaje dbPersonaje = null;
		Transaction tx = null;

		tx = session.beginTransaction();
		try {
			Query queryUsuario = session.createQuery("from PaqueteUsuario where usuario = :userName ");
			queryUsuario.setParameter("userName", user.getUsername());
			PaqueteUsuario dbUser = (PaqueteUsuario) queryUsuario.getSingleResult();

			int idPersonaje = dbUser.getIdPj();

			// Selecciono los datos del personaje
			Query queryPersonaje = session.createQuery("from PaquetePersonaje where idPersonaje = :idPersonaje ");
			queryPersonaje.setParameter("idPersonaje", idPersonaje);
			dbPersonaje = (PaquetePersonaje) queryPersonaje.getSingleResult();

			// Traigo los id de los items correspondientes a mi personaje
			Query queryMochila = session.createQuery("FROM Mochila WHERE idMochila = :idMochila");
			queryMochila.setParameter("idMochila", dbPersonaje.getidMochila());
			Mochila mochila = (Mochila) queryMochila.getSingleResult();
			
			Query queryitem;

			int i = 2;
			int j = 0;
			Mochila resultadoItemsID;
			dbPersonaje.eliminarItems();	
			if (mochila != null) {
				
				

				while (j <= CANTITEMS) {
					if (mochila.getByItemId(i) != -1) {// si hay algo

						queryitem = session.createQuery("FROM ItemHb WHERE idItem = :idItem");
						queryitem.setParameter("idItem", mochila.getByItemId(i));

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
		return dbPersonaje;
	}

	/**
	 * Método encargado de traer los datos del usuario de la base de datos.
	 * 
	 * @param usuario
	 *            Nombre de usuario
	 * @return PaqueteUsuario con los datos del usuario
	 */
	public PaqueteUsuario getUsuario(final String usuario) {// HBN DONE!
		Session session = getSessionFactory().openSession();

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

		Session session = getSessionFactory().openSession();

		Transaction tx = null;

		int value = 0;
		
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE Mochila " + "SET item1 = :it1 ,item2 = :it2 ,item3 = :it3 ,item4 = :it4 ,"
							+ "item5 = :it5 ,item6 = :it6 ,item7 = :it7 ,item8 = :it8 ,"
							+ "item9 = :it9 ,item10 = :it10 ,item11 = :it11 ,item12 = :it12 ,"
							+ "item13 = :it13 ,item14 = :it14 ,item15 = :it15 ,item16 = :it16 ,"
							+ "item17 = :it17 ,item18 = :it18 ,item19 = :it19 ,item20 = :it20"
							+ " WHERE idMochila = :idMochila");
			// Seteo parametros items
			for (int i = 0; i < paquetePersonaje.getCantItems(); i++) {
				value = i + 1;
				query.setParameter("it" + value, paquetePersonaje.getItemID(i));
			}
			// seteo el resto vacio
			for (int j = paquetePersonaje.getCantItems(); j < CANTITEMSMAXMOCHILA; j++) {
				value = j + 1;
				query.setParameter("it" + value, -1);
			}
			query.setParameter("idMochila", paquetePersonaje.getidMochila());
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

		Session session = getSessionFactory().openSession();

		Transaction tx = null;

		int value = 0;
		
		try {
			PaquetePersonaje paquetePersonaje = Servidor.getPersonajesConectados().get(idPersonaje);
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE Mochila " + "SET item1 = :it1 ,item2 = :it2 ,item3 = :it3 ,item4 = :it4 ,"
							+ "item5 = :it5 ,item6 = :it6 ,item7 = :it7 ,item8 = :it8 ,"
							+ "item9 = :it9 ,item10 = :it10 ,item11 = :it11 ,item12 = :it12 ,"
							+ "item13 = :it13 ,item14 = :it14 ,item15 = :it15 ,item16 = :it16 ,"
							+ "item17 = :it17 ,item18 = :it18 ,item19 = :it19 ,item20 = :it20"
							+ " WHERE idMochila = :idMochila");

			// Seteo parametros items
		
			Query queryPersonaje = session.createQuery("from PaquetePersonaje where idPersonaje = :idPersonaje ");
			queryPersonaje.setParameter("idPersonaje", idPersonaje);
			PaquetePersonaje dbPersonaje = (PaquetePersonaje) queryPersonaje.getSingleResult();
			
			for (int i = 0; i < dbPersonaje.getCantItems(); i++) {
				value = i + 1;
				query.setParameter("it" + value, paquetePersonaje.getItemID(i));
			}
			int itemGanado = new Random().nextInt(CANTITEMS + CANTITEMSMAXMOCHILA) + 1;
			if (dbPersonaje.getCantItems() < CANTITEMS) {
				value = dbPersonaje.getCantItems() + 1;
				query.setParameter("it" + value, itemGanado);
				Servidor.log.append("Asigno" + System.lineSeparator());
			}
			// seteo el resto vacio
			for (int j = dbPersonaje.getCantItems() + 2; j <= CANTITEMSMAXMOCHILA; j++) {
				query.setParameter("it" + j, -1);
			}
			query.setParameter("idMochila", paquetePersonaje.getidMochila());
			
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

		Session session = getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("UPDATE PaquetePersonaje SET fuerza = :fuerza,"
					+ "destreza = :destreza, inteligencia = :inteligencia, saludTope = :saludTope, energiaTope = :energiaTope,"
					+ "experiencia = :experiencia, nivel= :nivel, puntosSkills= :puntosSkills, fuerzaSkill= :fuerzaSkill,"
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
