package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;

/**
 * Clase que se encarga de realizar el registro
 * de los usuarios.
 */
public class Registro extends ComandosServer {

    /**
     * Clase que se encarga del registro de los usuarios.
     */
    @Override
    public void ejecutar() {
        Paquete paqueteSv = new Paquete(null, 0);
        paqueteSv.setComando(Comando.REGISTRO);

        escuchaCliente.setPaqueteUsuario((PaqueteUsuario)
              (getGson().fromJson(getCadenaLeida(), PaqueteUsuario.class)).clone());

        // Si el usuario se pudo registrar le envio un msj de exito
        try {
            if (Servidor.getConector().registrarUsuario(
                    escuchaCliente.getPaqueteUsuario())) {
                paqueteSv.setMensaje(Paquete.getMsjExito());
                escuchaCliente.getSalida().writeObject(getGson().toJson(paqueteSv));

                // Si el usuario no se pudo registrar le envio un msj de fracaso
                } else {
                paqueteSv.setMensaje(Paquete.getMsjFracaso());
                escuchaCliente.getSalida().writeObject(getGson().toJson(paqueteSv));
              }
            } catch (IOException e) {
           Servidor.log.append("Falló al intentar enviar registro\n");
       }
    }
}
