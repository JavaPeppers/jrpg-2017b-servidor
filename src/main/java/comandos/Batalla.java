package comandos;

import java.io.IOException;

import estados.Estado;
import mensajeria.PaqueteBatalla;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Clase que se encarga de establecer la batalla
 * entre dos clientes.
 */
public class Batalla extends ComandosServer {

     /**
     * Método que establece el PaqueteBatalla entre los
     * clientes involucrados, y se los envía.
     * Setea el estado batalla en ambos clientes para que desaparezcan
     * del mapa, y se los envía a todos los clientes.
     */
    @Override
    public void ejecutar() {
        // Le reenvio al id del personaje batallado que quieren pelear
        escuchaCliente.setPaqueteBatalla((PaqueteBatalla)
              getGson().fromJson(getCadenaLeida(), PaqueteBatalla.class));
        Servidor.log.append(escuchaCliente.getPaqueteBatalla().getId()
                + " quiere batallar con "
                + escuchaCliente.getPaqueteBatalla().getIdEnemigo()
                + System.lineSeparator());
        try {
            // seteo estado de batalla
            Servidor.getPersonajesConectados().get(
                  escuchaCliente.getPaqueteBatalla().getId())
                  .setEstado(Estado.ESTADOBATALLA);
            Servidor.getPersonajesConectados().get(
                  escuchaCliente.getPaqueteBatalla().getIdEnemigo())
                  .setEstado(Estado.ESTADOBATALLA);
            escuchaCliente.getPaqueteBatalla().setMiTurno(true);
            escuchaCliente.getSalida().writeObject(
                   getGson().toJson(escuchaCliente.getPaqueteBatalla()));
            for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
                if (conectado.getIdPersonaje()
                    == escuchaCliente.getPaqueteBatalla().getIdEnemigo()) {
                    int aux = escuchaCliente.getPaqueteBatalla().getId();
                    escuchaCliente.getPaqueteBatalla().setId(
                           escuchaCliente.getPaqueteBatalla().getIdEnemigo());
                    escuchaCliente.getPaqueteBatalla().setIdEnemigo(aux);
                    escuchaCliente.getPaqueteBatalla().setMiTurno(false);
                    conectado.getSalida().writeObject(getGson().toJson(
                           escuchaCliente.getPaqueteBatalla()));
                    break;
                    }
                }
            } catch (IOException e) {
                Servidor.log.append("Falló al intentar enviar Batalla \n");
                }
        synchronized (Servidor.atencionConexiones) {
            Servidor.atencionConexiones.notify();
            }
        }
}