package comandos;

import java.io.IOException;

import mensajeria.PaqueteAtacar;
import servidor.EscuchaCliente;
import servidor.Servidor;
/**
 * Clase que se encarga de enviar al enemigo
 * el estado del personaje luego de atacar
 * durante la batalla.
 */
public class Atacar extends ComandosServer {

     /**
     * Método que envía el PaqueteAtacar
     * al enemigo, con el nuevo estado del personaje
     * rival.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaqueteAtacar((PaqueteAtacar)
             getGson().fromJson(getCadenaLeida(), PaqueteAtacar.class));
        for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
            if (conectado.getIdPersonaje()
                == escuchaCliente.getPaqueteAtacar().getIdEnemigo()) {
                try {
                    conectado.getSalida().writeObject(
                           getGson().toJson(escuchaCliente.getPaqueteAtacar()));
                    } catch (IOException e) {
                       Servidor.log.append("Falló al intentar enviar ataque a:"
                       + conectado.getPaquetePersonaje().getId() + "\n");
                    }
                }
            }
        }
}