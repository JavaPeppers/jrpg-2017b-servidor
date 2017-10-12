package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.PaqueteDeEnemigos;
import mensajeria.PaqueteDeMovimientosEnemigos;
import servidor.Servidor;

public class SetEnemigos extends ComandosServer{

	@Override
	public void ejecutar() {
		
		escuchaCliente.setPaqueteDeEnemigos((PaqueteDeEnemigos)gson.fromJson(cadenaLeida,PaqueteDeEnemigos.class));
		try {
			PaqueteDeEnemigos packEnemigos = new PaqueteDeEnemigos(Servidor.getEnemigos());
			packEnemigos.setComando(Comando.ACTUALIZARENEMIGOS);
			
			System.out.println("Creo paquete De Enemigos");
			escuchaCliente.getSalida().writeObject(gson.toJson(packEnemigos));
			
			System.out.println("Creo el paquete de ubicaciones de los enemigos");
			PaqueteDeMovimientosEnemigos packUb = new PaqueteDeMovimientosEnemigos(Servidor.getUbicacionEnemigos());
			packUb.setComando(Comando.MOVENEMIGOS);
			escuchaCliente.getSalida().writeObject(gson.toJson(packUb));
		} catch (IOException e) {
			Servidor.log.append("Error al setear a los enemigos en el mapa.\n");
		}
		
	}

}
