package servidor;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mensajeria.PaqueteEnemigo;
import mensajeria.PaqueteMensaje;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;

/**
 * Clase que se encarga de administrar y establecer
 * la conexión entre los clientes.
 */
public class Servidor extends Thread {

    /** ArraryList que tiene los clientes conectados. **/
    private static ArrayList<EscuchaCliente>
            clientesConectados = new ArrayList<>();

    /** Map que almacena la ubicación de los personajes. **/
    private static Map<Integer, PaqueteMovimiento>
           ubicacionPersonajes = new HashMap<>();

    /** Map que almacena los personajes conectados. **/
    private static Map<Integer, PaquetePersonaje>
           personajesConectados = new HashMap<>();

    /** Map que almacena la ubicación de los enemigos. **/
    private static Map<Integer, PaqueteEnemigo> enemigos = new HashMap<>();

    /** Hilo del servidor. **/
    private static Thread server;

    /** Socket del servidor. **/
    private static ServerSocket serverSocket;

    /** Conector con la base de datos.**/
    private static Conector conexionDB;

    /** Puerto. **/
    private int puerto;

    /** Variable que almacena el ancho. **/
    private static final int ANCHO = 700;

    /** Variable que almacena el alto. **/
    private static final int ALTO = 640;

    /** Variable que almacena el alto del frame del log. **/
    private static final int ALTO_LOG = 520;

    /** Variable que almacena el ancho del frame del log. **/
    private static final int ANCHO_LOG = ANCHO - 25;

    /** Variable que almacena la entrada del usuario. **/
    public static JTextArea log;

    /** Variable que atiende las conexiones. **/
    public static AtencionConexiones atencionConexiones;

    /** Variable que atiende los movimientos. **/
    public static AtencionMovimientos atencionMovimientos;

    /** Variable que indica el tamanio de la fuente. **/
    private static final int TAMFUENTE = 16;

    /** Variable que indica X. **/
    private static final int X = 10;

    /** Variable que indica Y. **/
    private static final int Y = 0;

    /** Variable que indica el ancho. **/
    private static final int WIDTH = 200;

    /** Variable que indica el alto. **/
    private static final int HEIGHT = 30;

    /**
     * Main.
     * @param args args
     */
     public static void main(final String[] args) {
        cargarInterfaz();
    }

    /**
     * Método que carga la interfaz.
     */
     private static void cargarInterfaz() {
        JFrame ventana = new JFrame("Servidor WOME");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(ANCHO, ALTO);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(null);
        ventana.setIconImage(Toolkit.getDefaultToolkit()
             .getImage("src/main/java/servidor/server.png"));
        JLabel titulo = new JLabel("Log del servidor...");
        titulo.setFont(new Font("Courier New", Font.BOLD, TAMFUENTE));
        titulo.setBounds(X, Y, WIDTH, HEIGHT);
        ventana.add(titulo);

		log = new JTextArea();
		log.setEditable(false);
		log.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		JScrollPane scroll = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(10, 40, ANCHO_LOG, ALTO_LOG);
		ventana.add(scroll);

		final JButton botonIniciar = new JButton();
		final JButton botonDetener = new JButton();
		botonIniciar.setText("Iniciar");
		botonIniciar.setBounds(220, ALTO - 70, 100, 30);
		botonIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server = new Thread(new Servidor());
				server.start();
				botonIniciar.setEnabled(false);
				botonDetener.setEnabled(true);
			}
		});

		ventana.add(botonIniciar);

		botonDetener.setText("Detener");
		botonDetener.setBounds(360, ALTO - 70, 100, 30);
		botonDetener.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					server.stop();
					atencionConexiones.stop();
					atencionMovimientos.stop();
					for (EscuchaCliente cliente : clientesConectados) {
						cliente.getSalida().close();
						cliente.getEntrada().close();
						cliente.getSocket().close();
					}
					serverSocket.close();
					log.append("El servidor se ha detenido." + System.lineSeparator());
				} catch (IOException e1) {
					log.append("Fallo al intentar detener el servidor." + System.lineSeparator());
				}
				if(conexionDB != null)
					conexionDB.close();
				botonDetener.setEnabled(false);
				botonIniciar.setEnabled(true);
			}
		});
		botonDetener.setEnabled(false);
		ventana.add(botonDetener);

		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.addWindowListener(new WindowAdapter() {
			@SuppressWarnings("deprecation")
			public void windowClosing(WindowEvent evt) {
				if (serverSocket != null) {
					try {
						server.stop();
						atencionConexiones.stop();
						atencionMovimientos.stop();
						for (EscuchaCliente cliente : clientesConectados) {
							cliente.getSalida().close();
							cliente.getEntrada().close();
							cliente.getSocket().close();
						}
						serverSocket.close();
						log.append("El servidor se ha detenido." + System.lineSeparator());
					} catch (IOException e) {
						log.append("Fallo al intentar detener el servidor." + System.lineSeparator());
						System.exit(1);
					}
				}
				if (conexionDB != null)
					conexionDB.close();
				System.exit(0);
			}
		});

		ventana.setVisible(true);
	}

	public void run() {
	    
	    try {
			puerto = getPuerto();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    System.out.println(puerto);
		try {
			conexionDB = new Conector();
			conexionDB.connect();
			
			log.append("Iniciando el servidor..." + System.lineSeparator());
			serverSocket = new ServerSocket(puerto);
			log.append("Servidor esperando conexiones..." + System.lineSeparator());
			String ipRemota;
			
			atencionConexiones = new AtencionConexiones();
			atencionMovimientos = new AtencionMovimientos();
			
			atencionConexiones.start();
			atencionMovimientos.start();
			
			//Ubico a los enemigos en el hashMap de enemigos
			for(int i=-1;i>=-10;i--) {
				
				float x = (float)Math.random()*(250-1500)+1500;
				float y = (float)Math.random()*(x/2-1600)+1600;
				PaqueteEnemigo paqueteEnemigo = new PaqueteEnemigo(i,x,y);
				enemigos.put(i, paqueteEnemigo);
				
				setEnemigos(enemigos);
			}
			
			while (true) {
				Socket cliente = serverSocket.accept();
				ipRemota = cliente.getInetAddress().getHostAddress();
				log.append(ipRemota + " se ha conectado" + System.lineSeparator());

				ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream());
				ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

				EscuchaCliente atencion = new EscuchaCliente(ipRemota, cliente, entrada, salida);
				atencion.start();
				clientesConectados.add(atencion);					
			}
		} catch (Exception e) {
			log.append("Fallo la conexión." + System.lineSeparator());
		}
	}

	
	public static boolean mensajeAUsuario(PaqueteMensaje pqm) {
		boolean encontro = false;
		for (Map.Entry<Integer, PaquetePersonaje> personaje : personajesConectados.entrySet()) {
			if(!encontro && (personaje.getValue().getNombre().equals(pqm.getUserReceptor()))) {
				encontro = true;
			} 
		}
		// Si existe inicio sesion
		if (encontro) {
			Servidor.log.append(pqm.getUserEmisor() + " envió mensaje a " + pqm.getUserReceptor() + System.lineSeparator());
				return true;
		} 
		// Si no existe informo y devuelvo false
		Servidor.log.append("El mensaje para " + pqm.getUserReceptor() + " no se envió, ya que se encuentra desconectado." + System.lineSeparator());
		return false;
	}
	
	public static boolean mensajeAAll(int contador) {
		// Si existe inicio sesion
		if (!(personajesConectados.size() != contador+1)) {
			Servidor.log.append("Se ha enviado un mensaje a todos los usuarios" + System.lineSeparator());
				return true;
		} else {
			// Si no existe informo y devuelvo false
			Servidor.log.append("Uno o más de todos los usuarios se ha desconectado, se ha mandado el mensaje a los demas." + System.lineSeparator());
			return false;
		}
	}
	
	public static ArrayList<EscuchaCliente> getClientesConectados() {
		return clientesConectados;
	}

	public static Map<Integer, PaqueteMovimiento> getUbicacionPersonajes() {
		return ubicacionPersonajes;
	}
	
	public static Map<Integer, PaquetePersonaje> getPersonajesConectados() {
		return personajesConectados;
	}

	public static Conector getConector() {
		return conexionDB;
	}

	public static Map<Integer, PaqueteEnemigo> getEnemigos() {
		return enemigos;
	}
	
	public static void setEnemigos(Map<Integer, PaqueteEnemigo> enemigos) {
		Servidor.enemigos = enemigos;
	}
	
	public int getPuerto() throws FileNotFoundException {
        int puerto;
        Scanner sc = new Scanner(new File("puerto.properties"));
        sc.next();
        sc.next();
        puerto = sc.nextInt();
        sc.close();
        return puerto;
    }
	
}