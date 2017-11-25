package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;

/**
 * Clase que se encarga del inicio de sesión.
 */
public class InicioSesion extends ComandosServer {

     /**
     * Método que se encarga de cargar los datos del usuario
     * que va a iniciar sesión.
     */
    @Override
    public void ejecutar() {
        Paquete paqueteSv = new Paquete(null, 0);
        paqueteSv.setComando(Comando.INICIOSESION);
        // Recibo el paquete usuario
        escuchaCliente.setPaqueteUsuario((PaqueteUsuario)
            (getGson().fromJson(getCadenaLeida(), PaqueteUsuario.class)));
        // Si se puede loguear el usuario le envio un
        // mensaje de exito y el paquete personaje con los datos
        try {
            if (Servidor.getConector().loguearUsuario(
                escuchaCliente.getPaqueteUsuario())) {
                PaquetePersonaje paquetePersonaje = new PaquetePersonaje();
                paquetePersonaje = Servidor.getConector().
                       getPersonaje(escuchaCliente.getPaqueteUsuario());
                paquetePersonaje.setComando(Comando.INICIOSESION);
                paquetePersonaje.setMensaje(Paquete.getMsjExito());
                escuchaCliente.setIdPersonaje(paquetePersonaje.getId());
                paquetePersonaje.setFuerzaExtra(paquetePersonaje.getFuerza());
                escuchaCliente.getSalida().writeObject(
                       getGson().toJson(paquetePersonaje));
        } else {
             paqueteSv.setMensaje(Paquete.getMsjFracaso());
             escuchaCliente.getSalida().writeObject(getGson().toJson(paqueteSv));
             }
            } catch (IOException e) {
                Servidor.log.append("Falló al intentar iniciar sesión \n");
                }
    }
}
