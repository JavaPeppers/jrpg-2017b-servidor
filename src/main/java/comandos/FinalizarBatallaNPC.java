package comandos;

import java.io.IOException;

import estados.Estado;
import mensajeria.PaqueteFinalizarBatalla;
import mensajeria.PaqueteMovimiento;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class FinalizarBatallaNPC extends ComandosServer {

	@Override
	public void ejecutar() {
		
		PaqueteFinalizarBatalla paqueteFinalizarBatalla = (PaqueteFinalizarBatalla)gson.fromJson(cadenaLeida, PaqueteFinalizarBatalla.class); 
		escuchaCliente.setPaqueteFinalizarBatalla(paqueteFinalizarBatalla);
		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarBatalla().getId()).setEstado(Estado.estadoJuego);
		//Si gana el personaje actualizo su inventario y respawneo al enemigo
		if(paqueteFinalizarBatalla.getGanadorBatalla() == paqueteFinalizarBatalla.getId()) {
			Servidor.getConector().actualizarInventario(paqueteFinalizarBatalla.getGanadorBatalla());
			Servidor.getUbicacionEnemigos().remove(paqueteFinalizarBatalla.getIdEnemigo());
			
			PaqueteMovimiento newPosicion = new PaqueteMovimiento( paqueteFinalizarBatalla.getIdEnemigo() , 100 + ((float) Math.random() * 500), 10 +( (float)Math.random() * 500));
			
			Servidor.getUbicacionEnemigos().put( paqueteFinalizarBatalla.getIdEnemigo(), newPosicion);
		}
		
		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			if( conectado.getIdPersonaje() == escuchaCliente.getPaqueteFinalizarBatalla().getId() ){
				try {
					conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteFinalizarBatalla()));
				} catch (IOException e) {
					Servidor.log.append("Falló al intentar enviar finalizarBatalla a:" + conectado.getPaquetePersonaje().getId() + "\n");
				}
			}
		}
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
	}
		
}
