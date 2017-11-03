package comandos;

import java.io.IOException;
import java.util.Iterator;

import estados.Estado;
import mensajeria.Comando;
import mensajeria.PaqueteEnemigo;
import mensajeria.PaqueteMovimiento;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ActualizarEnemigo extends ComandosServer {

	/** Rango final X para el respawn. **/
	private static final int RANGOFINALX = 1500;

	/** Rango final Y para el respawn. **/
	private static final int RANGOFINALY = 1600;

	/** Rango de cercanía en el que el enemigo ataca al personaje. **/
	private static final int RANGODEVISTA = 500;

	/**
	 * Método que ejecuta el respawn del Enemigo.
	 **/
	@Override
	public void ejecutar() {
		// VER SI TIENE PERSONAJES CERCA
		escuchaCliente.setPaqueteEnemigo((PaqueteEnemigo) getGson().fromJson(getCadenaLeida(), PaqueteEnemigo.class));
		float x = 0;
		float y = 0;
		boolean agrego = false;
		while (!agrego) {
			Iterator<Integer> it = Servidor.getUbicacionPersonajes().keySet().iterator();
			int key;
			PaqueteMovimiento actual;
			x = (float) Math.random() * (0 - RANGOFINALX) + RANGOFINALX;
			y = (float) Math.random() * (x / 2 - RANGOFINALY) + RANGOFINALY;
			boolean personajeCerca = false;
			while (it.hasNext() && !personajeCerca) {
				key = it.next();
				actual = Servidor.getUbicacionPersonajes().get(key);
				if (actual != null) {
					if (Math.sqrt(
							Math.pow(actual.getPosX() - x, 2) + Math.pow(actual.getPosY() - y, 2)) <= RANGODEVISTA) {
						personajeCerca = true;
					}
				}
			}
			if (!personajeCerca) {
				agrego = true;
			}
		}
		if (agrego) {
			int idEnemigo = escuchaCliente.getPaqueteEnemigo().getId();
			Servidor.getEnemigos().get(idEnemigo).setX(x);
			Servidor.getEnemigos().get(idEnemigo).setY(y);
			Servidor.getEnemigos().get(idEnemigo).setEstado(Estado.ESTADOJUEGO);
			escuchaCliente.setPaqueteEnemigo(Servidor.getEnemigos().get(idEnemigo));
			escuchaCliente.getPaqueteEnemigo().setComando(Comando.ACTUALIZARENEMIGO);
			for(EscuchaCliente conectado: Servidor.getClientesConectados()) {
				try {
					conectado.getSalida().writeObject(
		                    getGson().toJson(escuchaCliente.getPaqueteEnemigo()));
				} catch (IOException e) {
					Servidor.log.append("Falló al intentar enviar "
		                    + "paqueteEnemigo a:" + conectado.getPaquetePersonaje().
		                    getId() + "\n");
				}
			}

		}
	}
	
}
