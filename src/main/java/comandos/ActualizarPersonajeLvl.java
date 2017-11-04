package comandos;

import java.io.IOException;

import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Clase que se encarga de enviar a los clientes
 * el nivel actualizado del personaje a todos
 * los clientes.
 */
public class ActualizarPersonajeLvl extends ComandosServer {

     /**
     * Método que envía el nuevo PaquetePersonaje actualizado
     * a todos los clientes.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
              getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class));
        Servidor.getConector().actualizarPersonajeSubioNivel(
              escuchaCliente.getPaquetePersonaje());
        Servidor.getPersonajesConectados().remove(
              escuchaCliente.getPaquetePersonaje().getId());
        Servidor.getPersonajesConectados().put(
              escuchaCliente.getPaquetePersonaje().getId(),
              escuchaCliente.getPaquetePersonaje());
        escuchaCliente.getPaquetePersonaje().ponerBonus();
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
            try {
                conectado.getSalida().writeObject(
                getGson().toJson(escuchaCliente.getPaquetePersonaje()));
            } catch (IOException e) {
                Servidor.log.append("Falló al intentar enviar"
                + "paquetePersonaje a:" +
                conectado.getPaquetePersonaje().getId() + "\n");
            }
       }
   }
}
