package comandos;

import estados.Estado;
import mensajeria.PaqueteBatallaNPC;
import servidor.Servidor;
/**
 * Clase que se encarga de establecer la batalla
 * entre el cliente y el Enemigo (NPC).
 *
 */
public class BatallaNPC extends ComandosServer {
/**
 * Método que establece el paqueteBatalla entre
 * el cliente y el NPC.
 * Setea ambos en "Estado Batalla" para que
 * desaparezcan del mapa, y se lo manda a todos los clientes.
 */
    @Override
    public void ejecutar() {

        escuchaCliente.setPaqueteBatallaNPC((PaqueteBatallaNPC)
              getGson().fromJson(getCadenaLeida(), PaqueteBatallaNPC.class));

        Servidor.log.append(escuchaCliente.getPaqueteBatallaNPC().getId()
              + " va a pelear con Enemigo de Id "
              + escuchaCliente.getPaqueteBatallaNPC().getIdEnemigo()
              + "\n");
        try {
            // seteo estado de batalla
            Servidor.getPersonajesConectados().get(
                    escuchaCliente.getPaqueteBatallaNPC().getId())
                    .setEstado(Estado.ESTADOBATALLANPC);
            Servidor.getEnemigos().get(escuchaCliente.getPaqueteBatallaNPC()
                    .getIdEnemigo()).setEstado(Estado.ESTADOBATALLANPC);
            escuchaCliente.getPaqueteBatallaNPC().setMiTurno(true);
            escuchaCliente.getSalida().writeObject(getGson().toJson(
                 escuchaCliente.getPaqueteBatallaNPC()));

        } catch (Exception e) {
             Servidor.log.append("Falló al intentar enviar Batalla NPC \n");
        }

        synchronized (Servidor.atencionConexiones) {
           Servidor.atencionConexiones.notify();
        }
    }
}