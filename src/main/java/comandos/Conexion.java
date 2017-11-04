package comandos;

import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import servidor.Servidor;
/**
 * Clase que se encarga de establecer la conexión
 * de los personajes.
 */
public class Conexion extends ComandosServer {
    /**
     * Método que se encarga de agregar el personaje
     * a la lista de conectados y establece su ubiación.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
             (getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class)).clone());

        //Actualizo los personajes en la conexion.
        Servidor.getPersonajesConectados().put(
             escuchaCliente.getPaquetePersonaje().getId(),
             (PaquetePersonaje) escuchaCliente.getPaquetePersonaje().clone());
        Servidor.getUbicacionPersonajes().put(
             escuchaCliente.getPaquetePersonaje().getId(),
             (PaqueteMovimiento) new PaqueteMovimiento(
                   escuchaCliente.getPaquetePersonaje().getId()).clone());
        synchronized (Servidor.atencionConexiones) {
            Servidor.atencionConexiones.notify();
        }
    }
}
