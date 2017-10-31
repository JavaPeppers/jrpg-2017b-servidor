package comandos;

import mensajeria.Comando;
import servidor.EscuchaCliente;
/**
 * Clase que se encarga de establecer
 * la escucha con el cliente.
 */
public abstract class ComandosServer extends Comando {

    /**
     * Variable que tendrá todos los paquetes
     * provenientes del cliente.
     */
    protected EscuchaCliente escuchaCliente;

    /**
     * Método que recibe el escuchaCliente.
     */
    public void setEscuchaCliente(EscuchaCliente escuchaCliente) {
        this.escuchaCliente = escuchaCliente;
    }

}