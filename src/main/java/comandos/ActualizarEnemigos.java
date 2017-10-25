//package comandos;
//
//import java.io.IOException;
//import mensajeria.PaqueteEnemigo;
//import servidor.EscuchaCliente;
//import servidor.Servidor;
//
//public class ActualizarEnemigos extends ComandosServer{
//
//	@Override
//	public void ejecutar() {
//		escuchaCliente.setPaqueteEnemigo((PaqueteEnemigo) gson.fromJson(cadenaLeida, PaqueteEnemigo.class));
//				
////		Servidor.getEnemigos().remove(escuchaCliente.getPaqueteEnemigo().getId());
////		Servidor.getEnemigos().put(escuchaCliente.getPaqueteEnemigo().getId(), escuchaCliente.getPaqueteEnemigo());
//
//		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
//			try {
//				escuchaCliente.getPaqueteEnemigo()
//				conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteEnemigo()));
//			} catch (IOException e) {
//				Servidor.log.append("Falló al intentar enviar paqueteEnemigo a:" + conectado.getPaquetePersonaje().getId() + "\n");
//			}
//		}
//		
//	}
//
//}
