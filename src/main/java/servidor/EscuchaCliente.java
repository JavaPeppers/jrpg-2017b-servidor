package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.google.gson.Gson;

import comandos.ComandosServer;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteAtacar;
import mensajeria.PaqueteBatalla;
import mensajeria.PaqueteDeEnemigos;
import mensajeria.PaqueteDeMovimientos;
import mensajeria.PaqueteDePersonajes;
import mensajeria.PaqueteEnemigo;
import mensajeria.PaqueteFinalizarBatalla;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

public class EscuchaCliente extends Thread {

	private final Socket socket;
	private final ObjectInputStream entrada;
	private final ObjectOutputStream salida;
	private int idPersonaje;
	private final Gson gson = new Gson();
	
	private PaquetePersonaje paquetePersonaje;
	private PaqueteMovimiento paqueteMovimiento;
	private PaqueteBatalla paqueteBatalla;
	private PaqueteAtacar paqueteAtacar;
	private PaqueteFinalizarBatalla paqueteFinalizarBatalla;
	private PaqueteUsuario paqueteUsuario;
	private PaqueteDeMovimientos paqueteDeMovimiento;
	private PaqueteDePersonajes paqueteDePersonajes;
	private PaqueteEnemigo paqueteEnemigo;
	private PaqueteDeEnemigos paqueteDeEnemigos;
	private final static int CANTIDADENEMIGOS = 10;


	public EscuchaCliente(String ip, Socket socket, ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		this.socket = socket;
		this.entrada = entrada;
		this.salida = salida;
		paquetePersonaje = new PaquetePersonaje();
		paqueteEnemigo = new PaqueteEnemigo();
	}

	public void run() {
		try {
			ComandosServer comand;
			Paquete paquete;
			Paquete paqueteSv = new Paquete(null, 0);
			paqueteUsuario = new PaqueteUsuario();
			inicializarEnemigos();

			String cadenaLeida = (String) entrada.readObject();
		
			while (!((paquete = gson.fromJson(cadenaLeida, Paquete.class)).getComando() == Comando.DESCONECTAR)){
								

				comand = (ComandosServer) paquete.getObjeto(Comando.NOMBREPAQUETE);
				comand.setCadena(cadenaLeida);
				comand.setEscuchaCliente(this);
				comand.ejecutar();
				cadenaLeida = (String) entrada.readObject();
			}

			entrada.close();
			salida.close();
			socket.close();

			Servidor.getPersonajesConectados().remove(paquetePersonaje.getId());
			Servidor.getUbicacionPersonajes().remove(paquetePersonaje.getId());
			Servidor.getClientesConectados().remove(this);
			if(Servidor.getPersonajesConectados().isEmpty()) {
				Servidor.getEnemigos().clear();
			}

			for (EscuchaCliente conectado : Servidor.getClientesConectados()) {
				paqueteDePersonajes = new PaqueteDePersonajes(Servidor.getPersonajesConectados());
				paqueteDePersonajes.setComando(Comando.CONEXION);
				conectado.salida.writeObject(gson.toJson(paqueteDePersonajes, PaqueteDePersonajes.class));
			}

			Servidor.log.append(paquete.getIp() + " se ha desconectado." + System.lineSeparator());

		} catch (IOException | ClassNotFoundException e) {
			Servidor.log.append("Error de conexion: " + e.getMessage() + System.lineSeparator());
		} 
	}
	
	private void inicializarEnemigos() {
		int posIniX = 2;
		int posIniY = 2;

		int decrementoX = 0;
		int incrementoY = 0;
		for (int i = 0; i <	10; i++) {
			
			if (i == 0) {
				Servidor.getEnemigos().put(i, new PaqueteEnemigo(i, "Enemigo", posIniX, posIniY));
			} else if (i < 7) {
				Servidor.getEnemigos().put(i, new PaqueteEnemigo(i, "Enemigo" + i, posIniX - decrementoX, posIniY + incrementoY));
			} else {

				Servidor.getEnemigos().put(i, new PaqueteEnemigo(i, "Enemigo" + i, posIniX - decrementoX, posIniY - incrementoY));
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}
	
	public ObjectInputStream getEntrada() {
		return entrada;
	}
	
	public ObjectOutputStream getSalida() {
		return salida;
	}
	
	public PaquetePersonaje getPaquetePersonaje(){
		return paquetePersonaje;
	}
	
	public int getIdPersonaje() {
		return idPersonaje;
	}

	public PaqueteBatalla getPaqueteBatalla() {
		return paqueteBatalla;
	}

	public void setPaqueteBatalla(PaqueteBatalla paqueteBatalla) {
		this.paqueteBatalla = paqueteBatalla;
	}

	public PaqueteAtacar getPaqueteAtacar() {
		return paqueteAtacar;
	}

	public void setPaqueteAtacar(PaqueteAtacar paqueteAtacar) {
		this.paqueteAtacar = paqueteAtacar;
	}

	public PaqueteFinalizarBatalla getPaqueteFinalizarBatalla() {
		return paqueteFinalizarBatalla;
	}

	public void setPaqueteFinalizarBatalla(PaqueteFinalizarBatalla paqueteFinalizarBatalla) {
		this.paqueteFinalizarBatalla = paqueteFinalizarBatalla;
	}

	public PaqueteDeMovimientos getPaqueteDeMovimiento() {
		return paqueteDeMovimiento;
	}

	public void setPaqueteDeMovimiento(PaqueteDeMovimientos paqueteDeMovimiento) {
		this.paqueteDeMovimiento = paqueteDeMovimiento;
	}

	public PaqueteDePersonajes getPaqueteDePersonajes() {
		return paqueteDePersonajes;
	}

	public void setPaqueteDePersonajes(PaqueteDePersonajes paqueteDePersonajes) {
		this.paqueteDePersonajes = paqueteDePersonajes;
	}

	public void setIdPersonaje(int idPersonaje) {
		this.idPersonaje = idPersonaje;
	}

	public void setPaquetePersonaje(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}

	public PaqueteUsuario getPaqueteUsuario() {
		return paqueteUsuario;
	}

	public void setPaqueteUsuario(PaqueteUsuario paqueteUsuario) {
		this.paqueteUsuario = paqueteUsuario;
	}
	
	public PaqueteEnemigo getPaqueteEnemigo() {
		return paqueteEnemigo;
	}

	public void setPaqueteEnemigo(PaqueteEnemigo paqueteEnemigo) {
		this.paqueteEnemigo = paqueteEnemigo;
	}
	
	public void enviarPaqueteEnemigo() {
		paqueteDeEnemigos = new PaqueteDeEnemigos(Servidor.getEnemigos());
		paqueteDeEnemigos.setComando(Comando.SETENEMIGOS);
		try {
			this.salida.writeObject(gson.toJson(paqueteDeEnemigos, PaqueteDeEnemigos.class));
		} catch (IOException e) {
			Servidor.log.append("Fallo al enviar paquete enemigo\n");
		}
	}

	public PaqueteMovimiento getPaqueteMovimiento() {
		return paqueteMovimiento;
	}

	public void setPaqueteMovimiento(PaqueteMovimiento paqueteMovimiento) {
		this.paqueteMovimiento = paqueteMovimiento;
	}


}

