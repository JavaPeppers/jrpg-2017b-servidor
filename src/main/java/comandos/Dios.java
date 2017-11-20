package comandos;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;

/**
 * The Class Dios.
 */
public class Dios extends ComandosServer {

	/* (non-Javadoc)
	 * @see mensajeria.Comando#ejecutar()
	 */
	@Override
	public void ejecutar() {
		PaquetePersonaje paquetePersonaje = (PaquetePersonaje) getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class);
		Servidor.getPersonajesConectados().get(paquetePersonaje.getId()).setModoDios();
		synchronized (Servidor.atencionConexiones) {
			Servidor.atencionConexiones.notify();
		}
	}
}
