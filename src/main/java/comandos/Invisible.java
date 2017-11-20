package comandos;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;

/**
 * The Class Invisible.
 */
public class Invisible extends ComandosServer {

	/* (non-Javadoc)
	 * @see mensajeria.Comando#ejecutar()
	 */
	@Override
	public void ejecutar() {
		PaquetePersonaje paquetePersonaje = (PaquetePersonaje) getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class);
		Servidor.getPersonajesConectados().get(paquetePersonaje.getId()).setModoInvisible();
		synchronized (Servidor.atencionConexiones) {
		Servidor.atencionConexiones.notify();
		}
	}
}
