package comandos;

import java.util.Iterator;

import estados.Estado;
import mensajeria.PaqueteFinalizarBatallaNPC;
import mensajeria.PaqueteMovimiento;
import servidor.Servidor;

public class FinalizarBatallaNPC extends ComandosServer {

	@Override
	public void ejecutar() {
		
		PaqueteFinalizarBatallaNPC paqueteFinalizarBatalla = (PaqueteFinalizarBatallaNPC)gson.fromJson(cadenaLeida, PaqueteFinalizarBatallaNPC.class); 
		escuchaCliente.setPaqueteFinalizarBatallaNPC(paqueteFinalizarBatalla);
		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarBatallaNPC().getId()).setEstado(Estado.estadoJuego);
		//VER SI TIENE PERSONAJES CERCA
		float x = 0;
		float y = 0;
		boolean agrego=false;
		while(!agrego){
			Iterator<Integer> it = Servidor.getUbicacionPersonajes().keySet().iterator();
			int key;
			PaqueteMovimiento actual;
			x = (float)Math.random()*(0-1500)+1500;
			y = (float)Math.random()*(x/2-1600)+1600;
			boolean personajeCerca = false;
			while (it.hasNext() && !personajeCerca) {
				key = it.next();
				actual = Servidor.getUbicacionPersonajes().get(key);
				if (actual != null) {
					if( Math.sqrt(Math.pow(actual.getPosX() - x, 2) + Math.pow(actual.getPosY() - y, 2))<=500)
						personajeCerca=true;
				}
			}
			if(personajeCerca==false)
				agrego=true;	
		}
		if(agrego) {
			int id_enemigo = escuchaCliente.getPaqueteFinalizarBatallaNPC().getIdEnemigo();
			Servidor.getEnemigos().get(id_enemigo).setX(x);
			Servidor.getEnemigos().get(id_enemigo).setY(y);
			Servidor.getEnemigos().get(id_enemigo).setEstado(Estado.estadoJuego);
		}
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
	}
		
}
