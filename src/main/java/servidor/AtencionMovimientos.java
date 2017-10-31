package servidor;

import com.google.gson.Gson;

import estados.Estado;
import mensajeria.Comando;
import mensajeria.PaqueteDeMovimientos;

/**
 * Clase que se encarga de enviar a todos los clientes
 * la ubicación de los personajes.
 */
public class AtencionMovimientos extends Thread {

    /** gson utilizado para enviar los paquetes. **/
    private final Gson gson = new Gson();

    /** Constructor. **/
    public AtencionMovimientos() {

    }

    /**
     * Método que se ejecutará en segundo plano.
     * Envía la ubicación de los personajes.
     */
    public void run() {

        synchronized (this) {

            try {

              while (true) {

                    // Espero a que se conecte alguien
                    wait();

                    // Le reenvio la conexion a todos
                    for (EscuchaCliente conectado
                          : Servidor.getClientesConectados()) {

                     if (conectado.getPaquetePersonaje().getEstado()
                          == Estado.estadoJuego) {

                            //Envio la ubicacion de todos los personajes
                            PaqueteDeMovimientos pdp = (PaqueteDeMovimientos)
                                  new PaqueteDeMovimientos(Servidor
                                      .getUbicacionPersonajes()).clone();
                            pdp.setComando(Comando.MOVIMIENTO);
                            synchronized (conectado) {
                                  conectado.getSalida().writeObject(
                                     gson.toJson(pdp));
                                  }
                            }
                     }
                    }
              } catch (Exception e) {
                   Servidor.log.append("Falló al intentar enviar"
                      + "paqueteDeMovimientos \n");
                   }
            }
        }
}