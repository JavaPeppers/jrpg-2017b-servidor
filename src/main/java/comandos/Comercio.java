package comandos;

import java.io.IOException;

import mensajeria.PaqueteComerciar;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Clase que se encarga de establecer el comercio
 * entre dos clientes.
 */
public class Comercio extends ComandosServer  {

    /**
     * Método que obtiene el paqueteComerciar y se lo
     * envía al cliente involucrado en el comercio.
     */
    @Override
    public void ejecutar() {
        PaqueteComerciar paqueteComerciar;
        paqueteComerciar = (PaqueteComerciar)
             getGson().fromJson(getCadenaLeida(), PaqueteComerciar.class);

        //BUSCO EN LAS ESCUCHAS AL QUE SE LO TENGO QUE MANDAR
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
            if (conectado.getPaquetePersonaje().getId()
                == paqueteComerciar.getIdEnemigo()) {
                try {
                     conectado.getSalida().writeObject(
                         getGson().toJson(paqueteComerciar));
                    } catch (IOException e) {

                  Servidor.log.append("Falló al intentar enviar comercio a:"
                    + conectado.getPaquetePersonaje().getId()
                    + "\n");
                  }
                }
            }
        }
}
