package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.PaqueteDeEnemigos;
import mensajeria.PaquetePersonaje;
import servidor.Servidor;

/**
 * Clase que se encarga de setear los enemigos (NPC) en el mapa.
 */
public class SetEnemigos extends ComandosServer {

    /** Variable que indica el id del ultimo enemigo en el vector. **/
    private static final int FIN_ENEMIGO = -20;

    /** Variable que indica el id del primer enemigo en el vector. **/
    private static final int INI_ENEMIGO = -10;

    /**
     * Método que ubica los NPC y los envía a todos los clientes.
    */
    @Override
    public void ejecutar() {

        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
             getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class));
        try {
            PaqueteDeEnemigos packEnemigos = new PaqueteDeEnemigos();
            int aux = escuchaCliente.getPaquetePersonaje().getMapa();
            if (aux == 1) {
                for (int i = -1; i >= INI_ENEMIGO; i--) {
                     packEnemigos.getEnemigos()
                     .put(i, Servidor.getEnemigos().get(i));
                }
                packEnemigos.getEnemigos().put(-21, Servidor.getEnemigos().get(-21));
            }
            if (aux == 2) {
                for (int i = (INI_ENEMIGO - 1); i >= FIN_ENEMIGO; i--) {
                    packEnemigos.getEnemigos()
                    .put(i, Servidor.getEnemigos().get(i));
                }
            }
            packEnemigos.setComando(Comando.SETENEMIGOS);
            escuchaCliente.getSalida().writeObject(
                    getGson().toJson(packEnemigos));
        } catch (IOException e) {
            Servidor.log.append("Error al setear a los enemigos en el mapa.\n");
       }
    }
}
