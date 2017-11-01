package comandos;
import java.util.Iterator;

import estados.Estado;
import mensajeria.PaqueteFinalizarBatallaNPC;
import mensajeria.PaqueteMovimiento;
import servidor.Servidor;

/**
 * Clase que se encarga de finalizar la batalla con un NPC. Se respawnea Enemigo
 * en una nueva posición del mapa.
 *
 */
public class FinalizarBatallaNPC extends ComandosServer {

    /** Rango final X para el respawn. **/
    private static final int RANGOFINALX = 1500;

    /** Rango final Y para el respawn. **/
    private static final int RANGOFINALY = 1600;

    /** Rango de cercanía en el que el enemigo ataca al personaje. **/
    private static final int RANGODEATAQUE = 500;

    /**
    * Método que ejecuta el respawn del Enemigo.
    **/
    @Override
    public void ejecutar() {
        PaqueteFinalizarBatallaNPC paqueteFinalizarBatalla =
                (PaqueteFinalizarBatallaNPC) getGson().
                fromJson(getCadenaLeida(), PaqueteFinalizarBatallaNPC.class);
                escuchaCliente.setPaqueteFinalizarBatallaNPC(
                paqueteFinalizarBatalla);
        Servidor.getPersonajesConectados().get(
                escuchaCliente.getPaqueteFinalizarBatallaNPC().getId())
                .setEstado(Estado.ESTADOJUEGO);
        // VER SI TIENE PERSONAJES CERCA
        float x = 0;
        float y = 0;
        boolean agrego = false;
        while (!agrego) {
            Iterator<Integer> it = Servidor.getUbicacionPersonajes()
                .keySet().iterator();
            int key;
            PaqueteMovimiento actual;
            x = (float) Math.random()
                * (0 - RANGOFINALX) + RANGOFINALX;
            y = (float) Math.random() * (x / 2 - RANGOFINALY) + RANGOFINALY;
            boolean personajeCerca = false;
            while (it.hasNext() && !personajeCerca) {
                key = it.next();
            actual = Servidor.getUbicacionPersonajes().get(key);
                if (actual != null) {
                     if (Math.sqrt(Math.pow(actual.getPosX() - x, 2)
                        + Math.pow(actual.getPosY() - y, 2)) <= RANGODEATAQUE) {
                        personajeCerca = true;
                     }
                }
            }
            if (!personajeCerca) {
                agrego = true;
            }
        }
        if (agrego) {
            int idEnemigo = escuchaCliente.
                  getPaqueteFinalizarBatallaNPC().getIdEnemigo();
            Servidor.getEnemigos().get(idEnemigo).setX(x);
            Servidor.getEnemigos().get(idEnemigo).setY(y);
            Servidor.getEnemigos().get(idEnemigo).setEstado(Estado.ESTADOJUEGO);
        }
        synchronized (Servidor.atencionConexiones) {
            Servidor.atencionConexiones.notify();
        }
     }
}
