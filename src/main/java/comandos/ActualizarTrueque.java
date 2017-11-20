package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Clase que se encarga de enviar el personaje actualizado
 * luego del trueque a todos los clientes.
 */
public class ActualizarTrueque extends ComandosServer {

     /**
     * Método que envía el nuevo paquetePersonaje a todos
     * los clientes.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
             getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class));
        Servidor.getConector().actualizarInventario(
             escuchaCliente.getPaquetePersonaje());
        Servidor.getConector().actualizarPersonaje(
             escuchaCliente.getPaquetePersonaje());
        Servidor.getPersonajesConectados().remove(
             escuchaCliente.getPaquetePersonaje().getId());
        Servidor.getPersonajesConectados().put(
             escuchaCliente.getPaquetePersonaje().
             getId(), escuchaCliente.getPaquetePersonaje());
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
             try {
             conectado.getSalida().writeObject(getGson().toJson(
                    escuchaCliente.getPaquetePersonaje()));
            } catch (IOException e) {
                  Servidor.log.append("Falló al intentar enviar"
                      + "actualizacion de trueque a:"
                + conectado.getPaquetePersonaje().getId() + "\n");
                  }
             }
        }
}
