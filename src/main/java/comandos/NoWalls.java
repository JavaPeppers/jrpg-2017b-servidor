package comandos;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;

public class NoWalls extends ComandosServer{

	@Override
	public void ejecutar() {
		PaquetePersonaje paquetePersonaje = (PaquetePersonaje) getGson().fromJson(getCadenaLeida(), PaquetePersonaje.class);
		Servidor.getPersonajesConectados().get(paquetePersonaje.getId()).setAtravesarParedes();
	}

}
