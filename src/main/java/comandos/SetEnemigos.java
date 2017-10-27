package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.PaqueteDeEnemigos;
import servidor.Servidor;

public class SetEnemigos extends ComandosServer{

	@Override
	public void ejecutar() {
		
		escuchaCliente.setPaqueteDeEnemigos((PaqueteDeEnemigos)gson.fromJson(cadenaLeida,PaqueteDeEnemigos.class));
		try {
			PaqueteDeEnemigos packEnemigos = new PaqueteDeEnemigos(Servidor.getEnemigos());
			packEnemigos.setComando(Comando.SETENEMIGOS);
			
			System.out.println("Envio los enemigos al cliente");
			escuchaCliente.getSalida().writeObject(gson.toJson(packEnemigos));
		} catch (IOException e) {
			Servidor.log.append("Error al setear a los enemigos en el mapa.\n");
		}
	}
}
