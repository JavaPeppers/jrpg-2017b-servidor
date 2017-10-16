package comandos;

import java.io.IOException;

import mensajeria.Comando;
import mensajeria.PaqueteDeMovimientosEnemigos;
import mensajeria.PaqueteMovimiento;
import servidor.Servidor;

public class SetUbEnemigos extends ComandosServer {

	@Override
	public void ejecutar() {
		
		
		escuchaCliente.setPaqueteMovimiento((PaqueteMovimiento) (gson.fromJson((String) cadenaLeida, PaqueteMovimiento.class)));
		
		try {
			PaqueteDeMovimientosEnemigos packUb = new PaqueteDeMovimientosEnemigos(Servidor.getUbicacionEnemigos());
			packUb.setComando(Comando.SETUBENEMIGOS);
			System.out.println("Envio la ubicacion de los enemigos");
			escuchaCliente.getSalida().writeObject(gson.toJson(packUb));
		} catch (IOException e) {
			Servidor.log.append("Fallo al enviar paquete UbEnemigos\n");
		}

	}
}
