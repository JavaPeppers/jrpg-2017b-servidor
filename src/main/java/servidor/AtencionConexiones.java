package servidor;

import com.google.gson.Gson;

import estados.Estado;
import mensajeria.Comando;
import mensajeria.PaqueteDeEnemigos;
import mensajeria.PaqueteDePersonajes;

/**
 * Clase que se encarga de administrar las conexiones
 * de los clientes.
 * Extiende de Thread.
 */
public class AtencionConexiones extends Thread {

   /** gson que se va a utilizar para enviar paquetes a los clientes. */
    private final Gson gson = new Gson();

    /** Constructor. */
    public AtencionConexiones() {

    }

    /**
     * Método que se ejecutara en segundo plano.
     * Envía paquete de personajes y enemigos a los clientes.
     */
    public void run() {

         synchronized (this) {
             try {

              while (true) {
                  // Espero a que se conecte alguien
                  wait();

                    // Le reenvio la conexion a todos
                    for (EscuchaCliente conectado
                        : Servidor.getClientesConectados()) {

                         if (conectado.getPaquetePersonaje().getEstado()
                               != Estado.ESTADOOFFLINE) {

                            PaqueteDePersonajes pdp = (PaqueteDePersonajes)
                                  new PaqueteDePersonajes(Servidor
                                     .getPersonajesConectados()).clone();
                            pdp.setComando(Comando.CONEXION);
                            synchronized (conectado) {
                                 conectado.getSalida().writeObject(
                                       gson.toJson(pdp));
                                 }

                            PaqueteDeEnemigos pde = (PaqueteDeEnemigos)
                                  new PaqueteDeEnemigos(
                                      Servidor.getEnemigos()).clone();
                            pde.setComando(Comando.SETENEMIGOS);
                            synchronized (conectado) {
                                 conectado.getSalida().writeObject(
                                      gson.toJson(pde));
                                 }
                            }
                         }
                    }
              } catch (Exception e) {
                   Servidor.log.append("Falló al intentar"
                   + "enviar paqueteDePersonajes\n");
              }
           }
         }
}