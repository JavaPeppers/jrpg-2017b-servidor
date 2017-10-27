package comandos;

import estados.Estado;
import mensajeria.PaqueteBatallaNPC;
import servidor.Servidor;

public class BatallaNPC extends ComandosServer{

	@Override
	public void ejecutar() {
		
		escuchaCliente.setPaqueteBatallaNPC((PaqueteBatallaNPC) gson.fromJson(cadenaLeida, PaqueteBatallaNPC.class));

		Servidor.log.append(escuchaCliente.getPaqueteBatallaNPC().getId() + " va a pelear con Enemigo de Id " + escuchaCliente.getPaqueteBatallaNPC().getIdEnemigo() + "\n");
		try {
			// seteo estado de batalla
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatallaNPC().getId())
					.setEstado(Estado.estadoBatallaNPC); 
			Servidor.getEnemigos().get(escuchaCliente.getPaqueteBatallaNPC().getIdEnemigo())
					.setEstado(Estado.estadoBatallaNPC);
			escuchaCliente.getPaqueteBatallaNPC().setMiTurno(true);
			escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatallaNPC()));

		} catch (Exception e) {
			Servidor.log.append("Falló al intentar enviar Batalla NPC \n");
		}
		
		synchronized (Servidor.atencionConexiones) {
			Servidor.atencionConexiones.notify();
		}
	}

}