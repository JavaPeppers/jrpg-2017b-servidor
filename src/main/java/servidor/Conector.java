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

import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.HibernateUtil;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

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
    public boolean registrarUsuario(final PaqueteUsuario user) {
    	
    	Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try
		{
			//TODO: usar HQL
			transaction = session.beginTransaction();
			List<PaqueteUsuario> uList = session.createQuery("FROM registro WHERE usuario= ? ").list();
			
			if (uList.isEmpty()) {
				PaqueteUsuario u = new PaqueteUsuario(user.getIdPj(),user.getUsername(),user.getPassword());      		
        		//Transaction transaction2 = null;// Hace falta otra?
        		try{
        			transaction = session.beginTransaction();
        			session.save(u);
        			transaction.commit();
        			Servidor.log.append("El usuario "
        	                + user.getUsername()
        	                + " se ha registrado."
        	                + System.lineSeparator());
        			return true;
        		}
        		catch(Exception ex){
        			Servidor.log.append("Error al intentar registrar el usuario "
        		            + user.getUsername() + System.lineSeparator());
        		            System.err.println(ex.getMessage());
        		            return false;
        		}
        		finally{
        			session.close();
        		}
			
			}
			else {
	               Servidor.log.append("El usuario "
	            	        + user.getUsername()
	            	        + " ya se encuentra en uso." + System.lineSeparator());
	            	                return false;
	      }			
		}catch(Exception e) {
			Servidor.log.append("Error al intentar registrar el usuario "
		            + user.getUsername() + System.lineSeparator());
		            System.err.println(e.getMessage());
		            return false;
		}          	            	            	            	                                 
   }

    //DEPRECADO
     public boolean OLDregistrarUsuario(final PaqueteUsuario user) {
        ResultSet result = null;
        try {
            PreparedStatement st1 = connect.prepareStatement(
                 "SELECT * FROM registro WHERE usuario= ? ");
            st1.setString(PARAM1, user.getUsername());
            result = st1.executeQuery();

            if (!result.next()) {

                PreparedStatement st = connect.prepareStatement(
                      "INSERT INTO registro (usuario, password,"
                      + "idPersonaje) VALUES (?,?,?)");
                st.setString(PARAM1, user.getUsername());
                st.setString(PARAM2, user.getPassword());
                st.setInt(PARAM3, user.getIdPj());
                st.execute();
                Servidor.log.append("El usuario "
                + user.getUsername()
                + " se ha registrado."
                + System.lineSeparator());
                return true;
        } else {
               Servidor.log.append("El usuario "
        + user.getUsername()
        + " ya se encuentra en uso." + System.lineSeparator());
                return false;
           }
        } catch (SQLException ex) {
            Servidor.log.append("Error al intentar registrar el usuario "
            + user.getUsername() + System.lineSeparator());
            System.err.println(ex.getMessage());
            return false;
        }
   }

    /**
     * Método encargado de registrar el pesonaje en la base de datos.
     * @param paquetePersonaje PaquetePersonaje con los datos
     * del personaje a registrar
     * @param paqueteUsuario PaqueteUsuario con los datos del
     * usuario al que hay que registrarle el personaje
     * @return boolean
     */
     public boolean registrarPersonaje(final PaquetePersonaje paquetePersonaje,
             final PaqueteUsuario paqueteUsuario) {

            try {

            // Registro al personaje en la base de datos
            PreparedStatement stRegistrarPersonaje = connect.prepareStatement(
                 "INSERT INTO personaje (idInventario, idMochila,casta,raza,"
                 + "fuerza,destreza,inteligencia,saludTope,energiaTope,nombre,"
                 + "experiencia,nivel,idAlianza,puntosSkills, fuerzaSkill,"
                 + "inteligenciaSkill, destrezaSkill)"
                 + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                 PreparedStatement.RETURN_GENERATED_KEYS);
            stRegistrarPersonaje.setInt(PARAM1, -1);
            stRegistrarPersonaje.setInt(PARAM2, -1);
            stRegistrarPersonaje.setString(PARAM3, paquetePersonaje.getCasta());
            stRegistrarPersonaje.setString(PARAM4, paquetePersonaje.getRaza());
            stRegistrarPersonaje.setInt(PARAM5,
                paquetePersonaje.getFuerza());
            stRegistrarPersonaje.setInt(PARAM6, paquetePersonaje.getDestreza());
            stRegistrarPersonaje.setInt(PARAM7,
                paquetePersonaje.getInteligencia());
            stRegistrarPersonaje.setInt(PARAM8,
                paquetePersonaje.getSaludTope());
            stRegistrarPersonaje.setInt(PARAM9,
                paquetePersonaje.getEnergiaTope());
            stRegistrarPersonaje.setString(PARAM10,
                paquetePersonaje.getNombre());
            stRegistrarPersonaje.setInt(PARAM11, 0);
            stRegistrarPersonaje.setInt(PARAM12, 1);
            stRegistrarPersonaje.setInt(PARAM13, -1);
            stRegistrarPersonaje.setInt(PARAM14, 0);
            stRegistrarPersonaje.setInt(PARAM15, 0);
            stRegistrarPersonaje.setInt(PARAM16, 0);
            stRegistrarPersonaje.setInt(PARAM17, 0);

            stRegistrarPersonaje.execute();

            // Recupero la última key generada
            ResultSet rs = stRegistrarPersonaje.getGeneratedKeys();
            if (rs != null && rs.next()) {

                // Obtengo el id
                int idPersonaje = rs.getInt(1);

                // Le asigno el id al paquete personaje que voy a devolver
                paquetePersonaje.setId(idPersonaje);

                // Le asigno el personaje al usuario
                PreparedStatement stAsignarPersonaje
                = connect.prepareStatement("UPDATE registro SET "
                       + "idPersonaje=? WHERE usuario=? AND password=?");
                stAsignarPersonaje.setInt(PARAM1, idPersonaje);
                stAsignarPersonaje.setString(PARAM2,
                      paqueteUsuario.getUsername());
                stAsignarPersonaje.setString(PARAM3,
                      paqueteUsuario.getPassword());
                stAsignarPersonaje.execute();

                // Por ultimo registro el inventario y la mochila
                if (this.registrarInventarioMochila(idPersonaje)) {
                    Servidor.log.append("El usuario "
                    + paqueteUsuario.getUsername() + " ha creado el personaje "
                    + paquetePersonaje.getId() + System.lineSeparator());
                    return true;
                    } else {
                    Servidor.log.append("Error al registrar la mochila y"
                          + "el inventario del usuario "
                          + paqueteUsuario.getUsername()
                          + " con el personaje"
                          + paquetePersonaje.getId() + System.lineSeparator());
                    return false;
               }
            }
            return false;

         } catch (SQLException e) {
            Servidor.log.append(
                 "Error al intentar crear el personaje "
                 + paquetePersonaje.getNombre() + System.lineSeparator());
            return false;
         }
}
    /**
     * Método que registra el inventario del personaje.
     * @param idInventarioMochila identificador del inventario
     * @return boolean
     */
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
        ResultSet result = null;
        try {
            // Busco usuario y contraseña
            PreparedStatement st = connect
                 .prepareStatement("SELECT * FROM registro WHERE "
                        + "usuario = ? AND password = ? ");
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            result = st.executeQuery();

            // Si existe inicio sesion
            if (result.next()) {
                Servidor.log.append("El usuario " + user.getUsername()
                + " ha iniciado sesión." + System.lineSeparator());
                return true;
            }

            // Si no existe informo y devuelvo false
            Servidor.log.append("El usuario " + user.getUsername()
            + " ha realizado un intento fallido de inicio de sesión."
            + System.lineSeparator());
            return false;

        } catch (SQLException e) {
            Servidor.log.append("El usuario " + user.getUsername()
            + " fallo al iniciar sesión." + System.lineSeparator());
            return false;
        }

    }

     /**
      * Método que acualiza los datos del personaje en la base de datos.
      * @param paquetePersonaje PaquetePersonaje con los datos del personaje
      */
     public void actualizarPersonaje(final PaquetePersonaje paquetePersonaje) {
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
     public PaqueteUsuario getUsuario(final String usuario) {
        ResultSet result = null;
        PreparedStatement st;

        try {
            st = connect.prepareStatement("SELECT * "
                   + "FROM registro WHERE usuario = ?");
            st.setString(1, usuario);
            result = st.executeQuery();

            String password = result.getString("password");
            int idPersonaje = result.getInt("idPersonaje");

            PaqueteUsuario paqueteUsuario = new PaqueteUsuario();
            paqueteUsuario.setUsername(usuario);
            paqueteUsuario.setPassword(password);
            paqueteUsuario.setIdPj(idPersonaje);

            return paqueteUsuario;
        } catch (SQLException e) {
            Servidor.log.append("Fallo al intentar recuperar el usuario "
        + usuario + System.lineSeparator());
            Servidor.log.append(e.getMessage() + System.lineSeparator());
        }

        return new PaqueteUsuario();
    }

     /**
      * Método encargado de actualizar el inventario del personaje.
      * @param paquetePersonaje datos del personaje a actualizar.
      */
     public void actualizarInventario(final PaquetePersonaje paquetePersonaje) {
        int i = 0;
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
         }
    }

    /**
     * Método que actualiza el inventario del personaje.
     * @param idPersonaje al que se le desea actualizar el inventario.
     */
     public void actualizarInventario(final int idPersonaje) {
        int i = 0;
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
         }
    }

     /**
      * Método encargado de actualizar el nivel del personaje.
      * @param paquetePersonaje datos del personaje a actualizar.
      */
     public void actualizarPersonajeSubioNivel(
          final PaquetePersonaje paquetePersonaje) {
         try {
            PreparedStatement stActualizarPersonaje = connect
                 .prepareStatement("UPDATE personaje SET fuerza=?,"
                 + "destreza=?, inteligencia=?, saludTope=?, energiaTope=?,"
                 + "experiencia=?, nivel=?, puntosSkills=?, fuerzaSkill=?,"
                 + "inteligenciaSkill=?, destrezaSkill=?"
                 + " WHERE idPersonaje=?");

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

            Servidor.log.append("El personaje " + paquetePersonaje.getNombre()
            + " se ha actualizado con éxito."  + System.lineSeparator());
         } catch (SQLException e) {
           Servidor.log.append("Fallo al intentar actualizar el personaje "
         + paquetePersonaje.getNombre()  + System.lineSeparator());
         }
    }
}
