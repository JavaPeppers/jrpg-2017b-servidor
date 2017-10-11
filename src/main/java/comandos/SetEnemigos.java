package comandos;

import java.io.IOException;
import mensajeria.PaqueteDeEnemigos;
import servidor.Servidor;

public class SetEnemigos extends ComandosServer {

	@Override
	public void ejecutar() {
		PaqueteDeEnemigos paqueteDeEnemigos = (PaqueteDeEnemigos) gson.fromJson(cadenaLeida, PaqueteDeEnemigos.class);
		System.out.println(Servidor.getEnemigos());
		paqueteDeEnemigos.getEnemigos().putAll(Servidor.getEnemigos());
		try {
			escuchaCliente.getSalida().writeObject(gson.toJson(paqueteDeEnemigos, PaqueteDeEnemigos.class));
		} catch (IOException e) {
			Servidor.log.append("Falló al intentar enviar PaqueteDeEnemigos\n");
		}
		
	}

}
