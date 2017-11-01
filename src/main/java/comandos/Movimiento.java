package comandos;

import mensajeria.PaqueteMovimiento;
import servidor.Servidor;

/**
 * Clase que se encarga de establecer la ubicación del personaje.
 *
 */
public class Movimiento extends ComandosServer {

    /**
     * Método que se encarga de ubicar el personaje.
     */
    @Override
    public void ejecutar() {
        escuchaCliente.setPaqueteMovimiento((PaqueteMovimiento)
             (getGson().fromJson((String) getCadenaLeida(), PaqueteMovimiento.class)));

        Servidor.getUbicacionPersonajes().get(escuchaCliente
             .getPaqueteMovimiento().getIdPersonaje()).setPosX(
             escuchaCliente.getPaqueteMovimiento().getPosX());
        Servidor.getUbicacionPersonajes().get(escuchaCliente
             .getPaqueteMovimiento().getIdPersonaje()).setPosY(
             escuchaCliente.getPaqueteMovimiento().getPosY());
        Servidor.getUbicacionPersonajes().get(escuchaCliente
             .getPaqueteMovimiento().getIdPersonaje()).setDireccion(
             escuchaCliente.getPaqueteMovimiento().getDireccion());
        Servidor.getUbicacionPersonajes().get(escuchaCliente
             .getPaqueteMovimiento().getIdPersonaje()).setFrame(
             escuchaCliente.getPaqueteMovimiento().getFrame());
        synchronized (Servidor.atencionMovimientos) {
             Servidor.atencionMovimientos.notify();
        }
   }
}