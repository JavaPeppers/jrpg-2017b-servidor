package comandos;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;

/**
 * Clase que se encarga de cargar el mapa elegido
 * por el cliente.
 */
public class MostrarMapas extends ComandosServer {

    /**
     * Método que establece el mapa elegido.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
             getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class));
        Servidor.log.append(escuchaCliente.getSocket().getInetAddress()
              .getHostAddress() + " ha elegido el mapa "
              + escuchaCliente.getPaquetePersonaje().getMapa()
              + System.lineSeparator());
    }
}