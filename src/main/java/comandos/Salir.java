package comandos;

import java.io.IOException;

import mensajeria.Paquete;
import servidor.Servidor;

/**
 * Clase que se encarga de desconectar al cliente.
 */
public class Salir extends ComandosServer {

    /**
     * Método encargado de cerrar el socket del cliente que se desconecta.
     * Lo elimino de la lista de clientes conectados.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Override
    public void ejecutar() {
        // Cierro todo
        try {
            escuchaCliente.getEntrada().close();
            escuchaCliente.getSalida().close();
            escuchaCliente.getSocket().close();
        } catch (IOException e) {
            Servidor.log.append("Falló al intentar salir \n");
        }

        // Lo elimino de los clientes conectados
        Servidor.getClientesConectados().remove(this);
        Paquete paquete = (Paquete) getGson().fromJson(getCadenaLeida(), Paquete.class);
        // Indico que se desconecto
        Servidor.log.append(paquete.getIp()
             + " se ha desconectado." + System.lineSeparator());
        
        synchronized(Servidor.atencionConexiones) {
        	Servidor.atencionConexiones.notify();
        }
     }
    	
}
