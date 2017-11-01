package comandos;

import java.io.IOException;
import estados.Estado;
import mensajeria.PaqueteFinalizarBatalla;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Clase que se encarga de finalizar la batalla con otro cliente.
 *
 */
public class FinalizarBatalla extends ComandosServer {

    @Override
    public final void ejecutar() {

        PaqueteFinalizarBatalla paqueteFinalizarBatalla =
              (PaqueteFinalizarBatalla) getGson().fromJson(getCadenaLeida(),
              PaqueteFinalizarBatalla.class);
        escuchaCliente.setPaqueteFinalizarBatalla(paqueteFinalizarBatalla);
        Servidor.getConector().actualizarInventario(
              paqueteFinalizarBatalla.getGanadorBatalla());
        Servidor.getPersonajesConectados().get(escuchaCliente.
        getPaqueteFinalizarBatalla().getId()).setEstado(Estado.ESTADOJUEGO);
        Servidor.getPersonajesConectados().get(escuchaCliente.
              getPaqueteFinalizarBatalla().getIdEnemigo()).
              setEstado(Estado.ESTADOJUEGO);
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
            if (conectado.getIdPersonaje() == escuchaCliente.
                getPaqueteFinalizarBatalla().getIdEnemigo()) {
                try {
                    conectado.getSalida().writeObject(getGson().toJson(
                          escuchaCliente.getPaqueteFinalizarBatalla()));
                } catch (IOException e) {
                  Servidor.log.append("Falló al intentar enviar"
                  + "finalizarBatalla a:"
                  + conectado.getPaquetePersonaje().getId() + "\n");
                  }
            }
        }
        synchronized (Servidor.atencionConexiones) {
            Servidor.atencionConexiones.notify();
        }
    }
}
