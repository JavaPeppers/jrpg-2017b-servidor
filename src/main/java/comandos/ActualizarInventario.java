package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;
/**
 * Clase que se encarga de actualizar el inventario
 * de cada personaje.
 */
public class ActualizarInventario extends ComandosServer {

    /**
     * Método que envía el paquetePersonaje actualizado
     * con su nuevo inventario al cliente.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
              gson.fromJson(cadenaLeida, PaquetePersonaje.class));
        Servidor.getConector().actualizarInventario(
              escuchaCliente.getPaquetePersonaje());
        Servidor.getPersonajesConectados().remove(
              escuchaCliente.getPaquetePersonaje().getId());
        Servidor.getPersonajesConectados().put(
              escuchaCliente.getPaquetePersonaje().getId(),
              escuchaCliente.getPaquetePersonaje());
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
            try {
               conectado.getSalida().writeObject(gson.toJson(
                    escuchaCliente.getPaquetePersonaje()));
            } catch (IOException e) {
                 Servidor.log.append("Falló al intentar"
                      + "enviar paquetePersonaje a:"
                      + conectado.getPaquetePersonaje().getId() + "\n");
                    }
            }
        }
}