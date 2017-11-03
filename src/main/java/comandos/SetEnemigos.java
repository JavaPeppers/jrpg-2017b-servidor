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
            if(aux == 1)
	            for(int i = -1; i>=-10; i--)
	            	packEnemigos.getEnemigos().put(i, Servidor.getEnemigos().get(i));
            if(aux == 2)
            	for(int i = -11; i>=-20; i--)
            		packEnemigos.getEnemigos().put(i, Servidor.getEnemigos().get(i));
            packEnemigos.setComando(Comando.SETENEMIGOS);
            escuchaCliente.getSalida().writeObject(getGson().toJson(packEnemigos));
        } catch (IOException e) {
            Servidor.log.append("Error al setear a los enemigos en el mapa.\n");
       }
    }
}