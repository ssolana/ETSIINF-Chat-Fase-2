package Servidor;
import Datos.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class InterfazServidor extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField txtEscribirMensaje;
	String Nombre = "";
	String NMatr = "";
	String ip = "88.1.221.160";
	final int puertoservidor = 5001;
	final int puertocliente = 5002;
	JTextArea textAreaChat;
	private JScrollPane scrollPane;
	private static Connection conn;
	Connection pruebaCn;
	int IDUltMensaje;
	int IDUltConversacion;
	
	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TFG;user=sa;password=1234;trustServerCertificate=true");
		} catch(Exception ex) {
			ex.printStackTrace();
			conn = null;
		}
		return conn;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazServidor frame = new InterfazServidor();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfazServidor() {
		setTitle("InterfazServidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 186);
		contentPane.add(scrollPane);
		
		textAreaChat = new JTextArea();
		scrollPane.setViewportView(textAreaChat);
		
		txtEscribirMensaje = new JTextField();
		txtEscribirMensaje.setBounds(10, 208, 275, 20);
		contentPane.add(txtEscribirMensaje);
		txtEscribirMensaje.setColumns(10);
		
		pruebaCn = getConnection();
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					PaqueteDatos datos = new PaqueteDatos();
					//datos.setIP("lo que sea");
					datos.setMensaje("Servidor: " + txtEscribirMensaje.getText());
					datos.setNombre("Servidor");
					datos.setIdOperacion(4);
					
					Socket socket_sr = new Socket("192.168.1.42", puertocliente);
					
					ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
					
					flujo_s.writeObject(datos);
					
					socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
					
					textAreaChat.append("Yo: " + txtEscribirMensaje.getText()); //Guardo el mensaje enviado en el chat del Servidor
					textAreaChat.append("\n");
					txtEscribirMensaje.setText(null);
					
				} catch(Exception ex) {
					System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
				}
			}
		});
		btnEnviar.setBounds(313, 207, 89, 23);
		contentPane.add(btnEnviar);
		
		idUltimoMensaje();
		idUltimaConversacion();
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		
		try {
			
			ServerSocket socket_servidor = new ServerSocket(puertoservidor);
			
			while(true) {
			
				Socket socket_cl = socket_servidor.accept();
				
				ObjectInputStream flujo_e = new ObjectInputStream(socket_cl.getInputStream());
				
				PaqueteDatos info = (PaqueteDatos) flujo_e.readObject();
				int idOp = info.getIdOperacion();
				
				if(idOp == 1) {
					boolean reg = registrarUsuario(info.getNMatr(),info.getNombre(),info.getPwd(),info.getIPpublica(),info.getIPprivada());
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setRegistro(reg);
						datos.setNMatr(NMatr);
						datos.setNombre(Nombre);
						datos.setIdOperacion(1);
						
						Socket socket_sr = new Socket(getIP(info.getNMatr()), puertocliente);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
						
					} catch(Exception ex) {
						System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
					}
				} else if(idOp == 2) {
					String nmatr = info.getNMatr();
					String pwd = info.getPwd();
					boolean IS = iniciarSesion(nmatr,pwd);
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setIS(IS);
						datos.setNMatr(nmatr);
						datos.setNombre(Nombre);
						datos.setIdOperacion(2);
						System.out.println(getIP(nmatr));
						Socket socket_sr = new Socket(getIP(nmatr), puertocliente);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
						
					} catch(Exception ex) {
						System.out.println("ERROR en iniciarSesion del Cliente: " + ex.getMessage());
					}
				} else if(idOp == 3) {
					String nmatr = info.getNMatr();
					ArrayList<String> chatsAbiertos = cargaConversaciones(nmatr);
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setChatsAbiertos(chatsAbiertos);
						datos.setIdOperacion(3);
						
						Socket socket_sr = new Socket(getIP(nmatr), puertocliente);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
						
					} catch(Exception ex) {
						System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
					}
				} else if(idOp == 4) {
					String mensaje = info.getMensaje();
					String NMatr = info.getNMatr();
					String nombre = info.getNombre();
					String idReceptor = getNMatr(NMatr, nombre);
					String ipReceptor = getIP(idReceptor);
					textAreaChat.append(mensaje);			//Guardo el mensaje en el registro de mensajes, es decir, en la ventana
					textAreaChat.append("\n");
					int id = idUltimoMensaje() + 1;
					int idConversacion = solicitarMensajes(NMatr,nombre);
					guardarMensaje(id,NMatr,idConversacion,mensaje);
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setMensaje(mensaje);
						datos.setIdOperacion(4);
						
						Socket socket_sr = new Socket(ipReceptor, puertocliente);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
						
					} catch(Exception ex) {
						System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
					}
					
				} else if(idOp == 5) {
					String nombre = info.getNombre();
					String nmatr = info.getNMatr();
					int idConversacion = solicitarMensajes(nmatr,nombre);
					ArrayList<String> mensajes = cargarMensajes(idConversacion);
					
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setMensajes(mensajes);
						datos.setIdOperacion(5);
						
						Socket socket_sr = new Socket(getIP(nmatr), puertocliente);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
						
					} catch(Exception ex) {
						System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
					}
					
				} else if(idOp == 6) {
					boolean existe = existeUsuario(info.getIdParticipante2(), info.getNombreParticipante2());
					if(existe == true) {
						String idP1 = info.getIdParticipante1();
						String idP2 = info.getIdParticipante2();
						String nombreP1 = info.getNombreParticipante1();
						String nombreP2 = info.getNombreParticipante2();
						int id = idUltimaConversacion() + 1;
						crearNuevoChat(id, idP1, idP2, nombreP1, nombreP2);
					}
					
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setExisteUsuario(existe);
						datos.setIdOperacion(6);
						
						Socket socket_sr = new Socket(getIP(info.getIdParticipante1()), puertocliente);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
						
					} catch(Exception ex) {
						System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
					}
					
				}
				
				socket_cl.close();
			}
			
		} catch(Exception ex) {
			System.out.println("Error en run() del Servidor: " + ex.getMessage());
		}
		
	}
	
	public String getIP(String id) {
		
		String ippublica = "";
		String ipprivada = "";
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT IPpublica,IPprivada FROM Usuarios WHERE NMatr='" + id + "'");
			if(rs.next()) {
				ippublica = rs.getString(1);
				ipprivada = rs.getString(2);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		if(ippublica.equals(ip)) {
			return ipprivada;
		} else {
			return ippublica;
		}
	}
	
	public ArrayList<String> cargaConversaciones(String nmatr){
		
		ArrayList<String> convers = new ArrayList<String>();
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Id,nombreParticipante2 AS Nombres FROM Conversaciones"
					+ " WHERE idParticipante1='" + nmatr + "' UNION SELECT Id,nombreParticipante1 FROM Conversaciones"
					+ " WHERE idParticipante2='" + nmatr + "'");
			while(rs.next()) {
				convers.add(rs.getString(2));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return convers;
	}
	
	public int solicitarMensajes(String nmatr, String nombre) {
		
		int codigoConversacion = 0;
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Id FROM Conversaciones WHERE"
					+ " (idParticipante1='" + nmatr+ "' AND nombreParticipante2='" + nombre + "') OR"
							+ " (idParticipante2='" + nmatr + "' AND nombreParticipante1='" + nombre + "')");
			if(rs.next()) {
				codigoConversacion = rs.getInt(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return codigoConversacion;
		
	}
	
	public ArrayList<String> cargarMensajes(int idConversacion) {
		
		ArrayList<String> mensajes = new ArrayList<String>();
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT mensaje FROM Mensajes WHERE idConversacion=" + idConversacion);
			while(rs.next()) {
				mensajes.add(rs.getString(1));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return mensajes;
				
	}
	
	public void guardarMensaje(int id, String idUsuario, int idConversacion, String mensaje) {
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("INSERT INTO Mensajes(Id,idUsuario,idConversacion,mensaje) values"
					+ " ("+ id +",'" + idUsuario + "'," + idConversacion + ",'" + mensaje + "')");
		} catch(Exception ex) {
			
		}
		
	}
	
	public int idUltimoMensaje() {
		
		int id = 0;
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Id FROM Mensajes ORDER BY Id DESC");
			if(rs.next()) {
				id = rs.getInt(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}
	
	public int idUltimaConversacion() {
		
		int id = 0;
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Id FROM Conversaciones ORDER BY Id DESC");
			if(rs.next()) {
				id = rs.getInt(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}
	
	public boolean iniciarSesion(String nmatr, String pwd) {
		
		boolean is = false;
		
		String nmatrres = "";
		String pwdres = "";
		String nombre = "";
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT NMatr,Password,Nombre FROM Usuarios WHERE NMatr='" + nmatr + "' AND "
					+ "Password='" + pwd + "'");
			if(rs.next()) {
				nmatrres = rs.getString(1);
				pwdres = rs.getString(2);
				nombre = rs.getString(3);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(nmatrres.equals(nmatr) && pwdres.equals(pwd)) {
			is = true;
			NMatr = nmatrres;
			Nombre = nombre;
		}
		
		return is;
	}
	
	public boolean registrarUsuario(String nmatr, String nombre, String pwd, String IPpublica, String IPprivada) {
		
		boolean regis = true;
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("INSERT INTO Usuarios(NMatr,Nombre,Password,IPpublica,IPprivada) values"
					+ " ('"+ nmatr +"','" + nombre + "','" + pwd + "','" + IPpublica + "','" + IPprivada + "')");
		} catch(Exception ex) {
			
		}
		
		NMatr = nmatr;
		Nombre = nombre;
		return regis;
	}
	
	public boolean existeUsuario(String id, String nombre) {
		
		boolean existe = false;
		
		String idres = "";
		String nombreres = "";
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT NMatr,Nombre FROM Usuarios WHERE NMatr='" + id + "' AND "
					+ "Nombre='" + nombre + "'");
			if(rs.next()) {
				idres = rs.getString(1);
				nombreres = rs.getString(2);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		if(idres.equals(id) && nombreres.equals(nombre)) {
			existe = true;
		}
		return existe;
		
	}
	
	public void crearNuevoChat(int id, String idP1, String idP2, String nombreP1, String nombreP2) {
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("INSERT INTO Conversaciones(Id,idParticipante1,nombreParticipante1,"
					+ "idParticipante2,nombreParticipante2) values"
					+ " ("+ id +",'" + idP1 + "','" + nombreP1 + "','" + idP2 + "','" + nombreP2 + "')");
		} catch(Exception ex) {
			
		}
		
	}
	
	public String getNMatr(String nmatremisor, String nombre) {
		
		String nmatrreceptor = "";
		
		try {
			Statement st = pruebaCn.createStatement();
			ResultSet rs = st.executeQuery("SELECT idParticipante2 FROM Conversaciones WHERE idParticipante1='" + nmatremisor + "'"
					+ " AND nombreParticipante2='" + nombre + "'\r\n" + 
					"UNION SELECT idParticipante1 FROM Conversaciones WHERE nombreParticipante1='" + nombre + "'"
							+ " AND idParticipante2='" + nmatremisor + "'");
			if(rs.next()) {
				nmatrreceptor = rs.getString(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return nmatrreceptor;
	}
	
}
