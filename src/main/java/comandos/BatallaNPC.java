package comandos;

import estados.Estado;
import mensajeria.PaqueteBatalla;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class BatallaNPC extends ComandosServer{

	@Override
	public void ejecutar() {
		// Le reenvio al id del personaje batallado que quieren pelear
		escuchaCliente.setPaqueteBatalla((PaqueteBatalla) gson.fromJson(cadenaLeida, PaqueteBatalla.class));

		Servidor.log.append(escuchaCliente.getPaqueteBatalla().getId() + " quiere batallar con "
				+ escuchaCliente.getPaqueteBatalla().getIdEnemigo() + System.lineSeparator());
		try {
			// seteo estado de batalla
			Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatalla().getId())
					.setEstado(Estado.estadoBatallaNPC);
			Servidor.getEnemigos().get(escuchaCliente.getPaqueteBatalla().getIdEnemigo())
					.setEstado(Estado.estadoBatallaNPC);
			escuchaCliente.getPaqueteBatalla().setMiTurno(true);
			escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatalla()));
			
			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				if (conectado.getIdPersonaje() == escuchaCliente.getPaqueteBatalla().getId()) {
					int aux = escuchaCliente.getPaqueteBatalla().getId();
					escuchaCliente.getPaqueteBatalla().setId(aux);
					escuchaCliente.getPaqueteBatalla().setIdEnemigo(escuchaCliente.getPaqueteBatalla().getIdEnemigo());
					escuchaCliente.getPaqueteBatalla().setMiTurno(false);
					conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatalla()));
					break;
				}
			}
		} catch (Exception e) {
			Servidor.log.append("Falló al intentar enviar Batalla \n");
		}
		
		synchronized (Servidor.atencionConexiones) {
			Servidor.atencionConexiones.notify();
		}

	}

}
