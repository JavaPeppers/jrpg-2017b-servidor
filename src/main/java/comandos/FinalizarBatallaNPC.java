//package comandos;
//
//import java.io.IOException;
//
////import estados.Estado;
////import mensajeria.PaqueteFinalizarBatalla;
////import mensajeria.PaqueteFinalizarBatallaNPC;
////import mensajeria.PaqueteMovimiento;
////import servidor.EscuchaCliente;
////import servidor.Servidor;
//
//public class FinalizarBatallaNPC extends ComandosServer {
//
//	@Override
//	public void ejecutar() {
//		
////		PaqueteFinalizarBatallaNPC paqueteFinalizarBatalla = (PaqueteFinalizarBatallaNPC)gson.fromJson(cadenaLeida, PaqueteFinalizarBatallaNPC.class); 
////		escuchaCliente.setPaqueteFinalizarNPC(paqueteFinalizarBatalla);
////		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarNPC().getId()).setEstado(Estado.estadoJuego);
////		//Si gana el personaje actualizo su inventario y respawneo al enemigo
////		if(paqueteFinalizarBatalla.getGanadorBatalla() == paqueteFinalizarBatalla.getId()) {
////			Servidor.getConector().actualizarInventario(paqueteFinalizarBatalla.getGanadorBatalla());
////			
////			
////			System.out.println("Llegue hasta aca?");
////			float aux = (float)Math.random()*(11-15)+15;
////			aux *=-1;
////			float x = Servidor.getUbicacionEnemigos().get((int)aux).getPosX();
////			float y = Servidor.getUbicacionEnemigos().get((int)aux).getPosY();
////			PaqueteMovimiento newPosicion = new PaqueteMovimiento( paqueteFinalizarBatalla.getIdEnemigo(),x,y);
////			
////			//Elimino esa posicion y le asigno otra HashMap me esta rompiendo..
////			Servidor.getUbicacionEnemigos().remove(paqueteFinalizarBatalla.getIdEnemigo());
////			Servidor.getUbicacionEnemigos().put( Servidor.getUbicacionEnemigos().size(), newPosicion);
////		}
//		
//		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
//			if( conectado.getIdPersonaje() == escuchaCliente.getPaqueteFinalizarNPC().getId() ){
//				try {
//					conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteFinalizarBatalla()));
//				} catch (IOException e) {
//					Servidor.log.append("Falló al intentar enviar finalizarBatalla a:" + conectado.getPaquetePersonaje().getId() + "\n");
//				}
//			}
//		}
//		synchronized(Servidor.atencionConexiones){
//			Servidor.atencionConexiones.notify();
//		}
//	}
//		
//}
