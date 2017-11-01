package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.PaqueteDeEnemigos;
import servidor.Servidor;

/**
 * Clase que se encarga de setear los enemigos (NPC) en el mapa.
 */
public class SetEnemigos extends ComandosServer {

    /**
     * Método que ubica los NPC y los envía a todos los clientes.
    */
    @Override
    public void ejecutar() {

        escuchaCliente.setPaqueteDeEnemigos((PaqueteDeEnemigos)
             getGson().fromJson(getCadenaLeida(), PaqueteDeEnemigos.class));
        try {
            PaqueteDeEnemigos packEnemigos =
                 new PaqueteDeEnemigos(Servidor.getEnemigos());
            packEnemigos.setComando(Comando.SETENEMIGOS);
            escuchaCliente.getSalida().writeObject(getGson().toJson(packEnemigos));
        } catch (IOException e) {
            Servidor.log.append("Error al setear a los enemigos en el mapa.\n");
       }
    }
}