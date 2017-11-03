package comandos;

import java.io.IOException;

import estados.Estado;
import mensajeria.Comando;
import mensajeria.PaqueteBatallaNPC;
import mensajeria.PaqueteEnemigo;
import servidor.EscuchaCliente;
import servidor.Servidor;
/**
 * Clase que se encarga de establecer la batalla
 * entre el cliente y el Enemigo (NPC).
 *
 */
public class BatallaNPC extends ComandosServer {
/**
 * Método que establece el paqueteBatalla entre
 * el cliente y el NPC.
 * Setea ambos en "Estado Batalla" para que
 * desaparezcan del mapa, y se lo manda a todos los clientes.
 */
    @Override
    public void ejecutar() {

        escuchaCliente.setPaqueteBatallaNPC((PaqueteBatallaNPC)
              getGson().fromJson(getCadenaLeida(), PaqueteBatallaNPC.class));
        Servidor.log.append(escuchaCliente.getPaqueteBatallaNPC().getId()
                + " esta batallando con el enemigo "
                + escuchaCliente.getPaqueteBatallaNPC().getIdEnemigo()
                + System.lineSeparator());
        try {
            // seteo estado de batalla en el servidor
            Servidor.getPersonajesConectados().get(
                    escuchaCliente.getPaqueteBatallaNPC().getId())
                    .setEstado(Estado.ESTADOBATALLANPC);
            Servidor.getEnemigos().get(escuchaCliente.getPaqueteBatallaNPC()
                    .getIdEnemigo()).setEstado(Estado.ESTADOBATALLANPC);
            escuchaCliente.getPaqueteBatallaNPC().setMiTurno(true);
            escuchaCliente.getSalida().writeObject(getGson().toJson(
                 escuchaCliente.getPaqueteBatallaNPC()));
            
            //LO QUE HAGO A PARTIR DE ACA ES DESAPARECERLE EL ENEMIGO A LOS OTROS CLIENTES
            //El otro cliente desaparece porque atencionConexiones 
            //le reenvia todo el tiempo el paquetePersonajes
            int idEnemigo = escuchaCliente.getPaqueteBatallaNPC().getIdEnemigo();
            
            PaqueteEnemigo paqueteEnemigo = Servidor.getEnemigos().get(idEnemigo);
            paqueteEnemigo.setComando(Comando.DESAPARECERENEMIGO);
            
            for (EscuchaCliente conectado : Servidor.getClientesConectados()) {	
                try {
                    conectado.getSalida().writeObject(getGson().toJson(paqueteEnemigo));
                } catch (IOException e) {
                  Servidor.log.append("Falló al intentar enviar"
                  + "DesaparecerEnemigo a:"
                  + conectado.getPaquetePersonaje().getId() + "\n");
                }
        	}
            

        } catch (Exception e) {
             Servidor.log.append("Falló al intentar enviar Batalla NPC \n");
        }

        synchronized (Servidor.atencionConexiones) {
           Servidor.atencionConexiones.notify();
        }
    }
}
