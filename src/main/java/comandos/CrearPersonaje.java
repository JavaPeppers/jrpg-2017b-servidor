package comandos;

import java.io.IOException;
import mensajeria.PaquetePersonaje;
import servidor.Servidor;

/**
 * Clase que se encarga de crear el personaje
 * y asignarlo a su correspondiente usuario.
 */
public class CrearPersonaje extends ComandosServer {

     /**
     * Método que recibe el paquetePersonaje y se lo asigna al usuario.
     * Crea el paquete personaje y se lo envía al cliente.
     */
    @Override
    public void ejecutar() {
        // Casteo el paquete personaje
        escuchaCliente.setPaquetePersonaje((PaquetePersonaje)
            (getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class)));
        // Guardo el personaje en ese usuario
        Servidor.getConector().registrarPersonaje(escuchaCliente
            .getPaquetePersonaje(), escuchaCliente.getPaqueteUsuario());
        try {
            PaquetePersonaje paquetePersonaje;
            paquetePersonaje = new PaquetePersonaje();
            paquetePersonaje = Servidor.getConector().getPersonaje(
                  escuchaCliente.getPaqueteUsuario());
            escuchaCliente.setIdPersonaje(paquetePersonaje.getId());
            escuchaCliente.getPaquetePersonaje().setFuerzaExtra(paquetePersonaje.getFuerza());
            escuchaCliente.getSalida().writeObject(getGson().toJson(
                  escuchaCliente.getPaquetePersonaje(),
                  escuchaCliente.getPaquetePersonaje().getClass()));
            } catch (IOException e1) {
                Servidor.log.append("Falló al intentar"
                    + "enviar personaje creado \n");
                }
        }
}
