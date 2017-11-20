package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.google.gson.Gson;

import comandos.ComandosServer;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteAtacar;
import mensajeria.PaqueteBatalla;
import mensajeria.PaqueteBatallaNPC;
import mensajeria.PaqueteDeEnemigos;
import mensajeria.PaqueteDeMovimientos;
import mensajeria.PaqueteDePersonajes;
import mensajeria.PaqueteEnemigo;
import mensajeria.PaqueteFinalizarBatalla;
import mensajeria.PaqueteFinalizarBatallaNPC;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

/**
 * Clase encargada de la comunicación con el cliente.
 */
public class EscuchaCliente extends Thread {

    /** Socket que se utilizará para la comunicación. **/
    private final Socket socket;

    /** Variable para obtener lo enviado por el cliente. **/
    private final ObjectInputStream entrada;

    /** Variable donde se guardará lo que se le va a enviar al cliente. **/
    private final ObjectOutputStream salida;

    /** Variable que almacena el id del personaje. **/
    private int idPersonaje;

    /** Variable que contiene el gson para la comunicación. **/
    private final Gson gson = new Gson();

    /** Variable que almacenará los datos del personaje. **/
    private PaquetePersonaje paquetePersonaje;

    /** Variable que almacenará los datos del enemigo. **/
    private PaqueteEnemigo paqueteEnemigo;

    /** Variable que almacenará el movimiento del personaje. **/
    private PaqueteMovimiento paqueteMovimiento;

    /** Variable que almacenará los datos de la batalla entre clientes. **/
    private PaqueteBatalla paqueteBatalla;

    /** Variable que almacenará los datos de la batalla entre cliente y NPC. **/
    private PaqueteBatallaNPC paqueteBatallaNPC;

    /** Variable que contendrá el ataque entre personajes. **/
    private PaqueteAtacar paqueteAtacar;

    /** Variable que almacenará los datos para finalizar una batalla. **/
    private PaqueteFinalizarBatalla paqueteFinalizarBatalla;

    /** Variable que almacenará los datos para finalizar
     * una batalla contra un NPC. **/
    private PaqueteFinalizarBatallaNPC paqueteFinalizarBatallaNPC;

    /** Variable que almacenará los datos del usuario. **/
    private PaqueteUsuario paqueteUsuario;

    /** Variable que almacenará los movimientos del personaje. **/
    private PaqueteDeMovimientos paqueteDeMovimiento;

    /** Variable que almacenará los personajes. **/
    private PaqueteDePersonajes paqueteDePersonajes;

    /** Variable que almacenará los enemigos. (NPC) **/
    private PaqueteDeEnemigos paqueteDeEnemigos;


    /**
     * Método que realiza la conexión con el cliente.
     * @param ip Dirección IP de conexión.
     * @param socketParam Socket.
     * @param entradaParam Objeto de entrada.
     * @param salidaParam Objeto de salida.
     * @throws IOException En el caso de no poder realizar la conexión.
     */
    public EscuchaCliente(final String ip, final Socket socketParam,
        final ObjectInputStream entradaParam, final ObjectOutputStream salidaParam)
            throws IOException {
        this.socket = socketParam;
        this.entrada = entradaParam;
        this.salida = salidaParam;
        paquetePersonaje = new PaquetePersonaje();
    }


    /**
     * Método encargado de escuchar los comandos del cliente.
     */
    public void run() {
         try {
            ComandosServer comand;
            Paquete paquete;
            //Paquete paqueteSv = new Paquete(null, 0);
            paqueteUsuario = new PaqueteUsuario();

            String cadenaLeida = (String) entrada.readObject();

            while (!((paquete = gson.fromJson(cadenaLeida,
            		Paquete.class)).getComando() == Comando.DESCONECTAR)) {


                comand = (ComandosServer)
                     paquete.getObjeto(Comando.NOMBREPAQUETE);
                comand.setCadena(cadenaLeida);
                comand.setEscuchaCliente(this);
                comand.ejecutar();
                cadenaLeida = (String) entrada.readObject();
            }

            entrada.close();
            salida.close();
            socket.close();

            Servidor.getPersonajesConectados().remove(paquetePersonaje.getId());
            Servidor.getUbicacionPersonajes().remove(paquetePersonaje.getId());
            Servidor.getClientesConectados().remove(this);

            for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
                paqueteDePersonajes = new PaqueteDePersonajes(
                    Servidor.getPersonajesConectados());
                paqueteDePersonajes.setComando(Comando.CONEXION);
                conectado.salida.writeObject(gson.toJson(
                    paqueteDePersonajes, PaqueteDePersonajes.class));

           }

           Servidor.log.append(paquete.getIp()
                + " se ha desconectado." + System.lineSeparator());

         } catch (IOException | ClassNotFoundException e) {
           Servidor.log.append("Error de conexion: "
         + e.getMessage() + System.lineSeparator());
       }
    }

    /**
     * Método que obtiene el socket.
     * @return Socket.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Método que obtiene la entrada.
     * @return ObjectInputStream Entrada.
     */
    public ObjectInputStream getEntrada() {
        return entrada;
    }

    /**
     * Método que obtiene la salida.
     * @return ObjectOutputStream Salida.
     */
    public ObjectOutputStream getSalida() {
        return salida;
    }

    /**
     * Método que me devuelve el paquetePersonaje.
     * @return PaquetePersonaje.
     */
    public PaquetePersonaje getPaquetePersonaje() {
       return paquetePersonaje;
    }

    /**
     * Método que me devuelve el ID del personaje.
     * @return int IdPersonaje.
     */
     public int getIdPersonaje() {
         return idPersonaje;
     }

     /**
      * Método que me devuelve el paqueteMovimiento.
      * @return PaqueteMovimiento.
      */
     public PaqueteMovimiento getPaqueteMovimiento() {
         return paqueteMovimiento;
    }

     /**
      * Método que setea el paqueteMovimiento.
      * @param paqueteMovimientoParam Movimiento.
      */
    public void setPaqueteMovimiento(
          final PaqueteMovimiento paqueteMovimientoParam) {
        this.paqueteMovimiento = paqueteMovimientoParam;
    }

    /**
     * Método que me devuelve el paqueteBatalla.
     * @return PaqueteBatalla.
     */
    public PaqueteBatalla getPaqueteBatalla() {
        return paqueteBatalla;
    }

    /**
     * Método que setea el paqueteBatalla.
     * @param paqueteBatallaParam PaqueteBatalla.
     */
    public void setPaqueteBatalla(final PaqueteBatalla paqueteBatallaParam) {
        this.paqueteBatalla = paqueteBatallaParam;
    }

    /**
     * Método que me devuelve el paqueteAtacar.
     * @return PaqueteAtacar paqueteAtacar.
     */
    public PaqueteAtacar getPaqueteAtacar() {
         return paqueteAtacar;
    }

    /**
     * Método que setea el paqueteAtacar.
     * @param paqueteAtacarParam PaqueteAtacar.
     */
    public void setPaqueteAtacar(final PaqueteAtacar paqueteAtacarParam) {
         this.paqueteAtacar = paqueteAtacarParam;
    }

    /**
     * Método que me devuelve el paqueteFinalizarBatalla.
     * @return PaqueteFinalizarBatalla paqueteFinalizarBatalla.
     */
    public PaqueteFinalizarBatalla getPaqueteFinalizarBatalla() {
         return paqueteFinalizarBatalla;
    }

    /**
     * Método que setea el paqueteFinalizarBatalla.
     * @param paqueteFinalizarBatallaParam paqueteFinalizarBatalla.
     */
    public void setPaqueteFinalizarBatalla(
           final PaqueteFinalizarBatalla paqueteFinalizarBatallaParam) {
         this.paqueteFinalizarBatalla = paqueteFinalizarBatallaParam;
    }

    /**
     * Método que devuelve el paquete de Movimientos.
     * @return PaqueteDeMovimiento
     */
    public PaqueteDeMovimientos getPaqueteDeMovimiento() {
        return paqueteDeMovimiento;
    }

    /**
     * Método que seta el paquete de movimientos.
     * @param paqueteDeMovimientoParam se setea el paquete de movimiento.
     */
    public void setPaqueteDeMovimiento(
        final PaqueteDeMovimientos paqueteDeMovimientoParam) {
    this.paqueteDeMovimiento = paqueteDeMovimientoParam;
    }

    /**
     * Método que me devuelve el paqueteDePersonajes.
     * @return PaqueteDePersonajes.
     */
    public PaqueteDePersonajes getPaqueteDePersonajes() {
         return paqueteDePersonajes;
    }

    /**
     * Méotdo que setea el paquete de personajes.
     * @param paqueteDePersonajesParam PaqueteDePersonajes.
     */
    public void setPaqueteDePersonajes(
          final PaqueteDePersonajes paqueteDePersonajesParam) {
       this.paqueteDePersonajes = paqueteDePersonajesParam;
    }

    /**
     * Método que setea el id del personaje.
     * @param idPersonajeParam int id de personaje.
     */
    public void setIdPersonaje(final int idPersonajeParam) {
         this.idPersonaje = idPersonajeParam;
    }

    /**
     * Método que setea el paquetePersonaje.
     * @param paquetePersonajeParam PaquetePersonaje
     */
    public void setPaquetePersonaje(
           final PaquetePersonaje paquetePersonajeParam) {
       this.paquetePersonaje = paquetePersonajeParam;
    }

    /**
     * Método que me devuelve el paqueteUsuario.
     * @return PaqueteUsuario.
     */
    public PaqueteUsuario getPaqueteUsuario() {
        return paqueteUsuario;
    }


    /**
     * Método que setea el paqueteUsuario.
     * @param paqueteUsuarioParam paq usuario.
     */
    public void setPaqueteUsuario(final PaqueteUsuario paqueteUsuarioParam) {
        this.paqueteUsuario = paqueteUsuarioParam;
    }

    /**
     * Método que me devuelve el paqueteEnemigo.
     * @return PaqueteEnemigo.
     */
    public PaqueteEnemigo getPaqueteEnemigo() {
         return paqueteEnemigo;
    }

    /**
     * Método que setea el paqueteEnemigo.
     * @param paqueteEnemigoParam paqueteEnemigo.
     */
    public void setPaqueteEnemigo(
       final PaqueteEnemigo paqueteEnemigoParam) {
       this.paqueteEnemigo = paqueteEnemigoParam;
   }

    /**
     * Método que me devuelve el paqueteDeEnemigos.
     * @return PaqueteDeEnemigos.
     */
    public PaqueteDeEnemigos getPaqueteDeEnemigos() {
        return paqueteDeEnemigos;
    }

    /**
     * Método que setea el paqueteDeEnemigos.
     * @param paqueteDeEnemigosParam paq de enemigos.
     */
    public void setPaqueteDeEnemigos(
         final PaqueteDeEnemigos paqueteDeEnemigosParam) {
       this.paqueteDeEnemigos = paqueteDeEnemigosParam;
    }

    /**
     * Método que me devuelve el paqueteFinalizarBatallaNPC.
     * @return PaqueteFinalizarBatallaNPC.
     */
     public PaqueteFinalizarBatallaNPC getPaqueteFinalizarBatallaNPC() {
         return paqueteFinalizarBatallaNPC;
     }

     /**
      * Método que setea el paqueteFinalizarBatallaNPC.
      * @param paqueteFinalizarNPC PaqueteFinalizarBatallaNPC.
      */
    public void setPaqueteFinalizarBatallaNPC(
        final PaqueteFinalizarBatallaNPC paqueteFinalizarNPC) {
       this.paqueteFinalizarBatallaNPC = paqueteFinalizarNPC;
    }

    /**
     * Método que me devuelve el paqueteBatallaNPC.
     * @return PaqueteBatallaNPC.
     */
    public PaqueteBatallaNPC getPaqueteBatallaNPC() {
        return paqueteBatallaNPC;
    }

    /**
     * Método que setea el paqueteBatallaNPC.
     * @param paqueteBatallaNPCParam PaqueteBatallaNPC.
     */
    public void setPaqueteBatallaNPC(
        final PaqueteBatallaNPC paqueteBatallaNPCParam) {
      this.paqueteBatallaNPC = paqueteBatallaNPCParam;
    }

}
