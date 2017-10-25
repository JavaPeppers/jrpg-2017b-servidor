package comandos;

import estados.Estado;
import mensajeria.PaqueteBatallaNPC;
import servidor.Servidor;

public class BatallaNPC extends ComandosServer{

	@Override
	public void ejecutar() {
		System.out.println("Llegue aca?");
		escuchaCliente.setPaqueteBatallaNPC((PaqueteBatallaNPC) gson.fromJson(cadenaLeida, PaqueteBatallaNPC.class));

		try {
			// seteo estado de batalla
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatallaNPC().getId())
					.setEstado(Estado.estadoBatallaNPC); 
			Servidor.getEnemigos().get(escuchaCliente.getPaqueteBatallaNPC().getIdEnemigo())
					.setEstado(Estado.estadoBatallaNPC);
			escuchaCliente.getPaqueteBatallaNPC().setMiTurno(true);
			System.out.println("Mando paquete batalla NPC a cliente");
			escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatallaNPC()));

		} catch (Exception e) {
			Servidor.log.append("Falló al intentar enviar Batalla NPC \n");
		}
		
		synchronized (Servidor.atencionConexiones) {
			Servidor.atencionConexiones.notify();
		}
	}

}