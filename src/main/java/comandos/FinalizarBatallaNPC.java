package comandos;

import estados.Estado;
import mensajeria.PaqueteFinalizarBatallaNPC;
import servidor.Servidor;

/**
 * Clase que se encarga de finalizar la batalla con un NPC. Se respawnea Enemigo
 * en una nueva posición del mapa.
 *
 */
public class FinalizarBatallaNPC extends ComandosServer {


   /**
    * Método que ejecuta finalizarBatallaNPC.
    */
    @Override
    public void ejecutar() {
        PaqueteFinalizarBatallaNPC paqueteFinalizarBatallaNPC =
                (PaqueteFinalizarBatallaNPC) getGson().
                fromJson(getCadenaLeida(), PaqueteFinalizarBatallaNPC.class);
        escuchaCliente.setPaqueteFinalizarBatallaNPC(
                paqueteFinalizarBatallaNPC);
        Servidor.getPersonajesConectados().get(
                escuchaCliente.getPaqueteFinalizarBatallaNPC(
                		).getId()).setEstado(Estado.ESTADOJUEGO);
        synchronized (Servidor.atencionConexiones) {
            Servidor.atencionConexiones.notify();
        }
     }

}
