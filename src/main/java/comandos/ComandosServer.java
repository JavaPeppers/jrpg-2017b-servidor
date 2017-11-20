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
     * @param escuchaClienteParam se setea la escucha cliente.
     */
    public void setEscuchaCliente(final EscuchaCliente escuchaClienteParam) {
        this.escuchaCliente = escuchaClienteParam;
    }

}
