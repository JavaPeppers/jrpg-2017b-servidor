package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Clase que se encarga de enviar a todos los clientes el estado
 * actualizado del personaje.
 */
public class ActualizarPersonaje extends ComandosServer {

    /**
     * Método que envía el paquetePersonaje
     * a los clientes.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
            getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class));
        Servidor.getConector().actualizarPersonaje(
            escuchaCliente.getPaquetePersonaje());
        Servidor.getPersonajesConectados().remove(
            escuchaCliente.getPaquetePersonaje().getId());
        Servidor.getPersonajesConectados().put(
            escuchaCliente.getPaquetePersonaje().getId(),
            escuchaCliente.getPaquetePersonaje());
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
            try {
                conectado.getSalida().writeObject(
                    getGson().toJson(escuchaCliente.getPaquetePersonaje()));
            } catch (IOException e) {
                Servidor.log.append("Falló al intentar enviar "
                    + "paquetePersonaje a: " + conectado.getPaquetePersonaje().
                    getId() + "\n");
                Servidor.log.append(e.getMessage() + "\n");
            }
        }
   }
}
