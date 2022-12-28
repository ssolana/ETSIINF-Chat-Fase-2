package Cliente;
import Datos.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JScrollPane;

public class JPanelChat extends JPanel {
	
	private JTextField txtEscribirMensaje;
	String usuarioActual;
	String nombreUsuarioActual;
	final String ip = "192.168.1.49";
	final int puertoservidor = 5001;
	final int puertocliente = 5002;
	JTextArea textAreaChat;
	JButton btnVolver;
	JLabel lblNombre;

	/**
	 * Create the panel.
	 */
	public JPanelChat() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(txtEscribirMensaje.getText().equals("")) {
					txtEscribirMensaje.setText("Escribir mensaje...");
					txtEscribirMensaje.select(0, 0);
				}
			}
		});
		setLayout(null);
		
		btnVolver = new JButton("Volver");
		btnVolver.setBackground(SystemColor.activeCaption);
		btnVolver.setBounds(336, 11, 88, 24);
		add(btnVolver);
		
		lblNombre = new JLabel("");
		lblNombre.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Entrega final\\Fotos dise\u00F1o app\\color titulo chat.PNG"));
		lblNombre.setBounds(20, 11, 400, 24);
		lblNombre.setHorizontalAlignment(SwingConstants.LEFT);
		lblNombre.setForeground(Color.BLACK);
		add(lblNombre);
		
		JLabel lblColorTituloChat = new JLabel("");
		lblColorTituloChat.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Fotos dise\u00F1o app\\color titulo chat.PNG"));
		lblColorTituloChat.setBounds(10, 11, 414, 24);
		lblColorTituloChat.setForeground(Color.WHITE);
		lblColorTituloChat.setBackground(Color.WHITE);
		add(lblColorTituloChat);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 57, 414, 170);
		add(scrollPane);
		
		textAreaChat = new JTextArea();
		scrollPane.setViewportView(textAreaChat);
		textAreaChat.setForeground(Color.BLACK);
		textAreaChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(txtEscribirMensaje.getText().equals("")) {
					txtEscribirMensaje.setText("Escribir mensaje...");
				}
			}
		});
		
		txtEscribirMensaje = new JTextField();
		txtEscribirMensaje.setBounds(10, 242, 312, 20);
		txtEscribirMensaje.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!txtEscribirMensaje.getText().equals("") && !txtEscribirMensaje.getText().equals("Escribir mensaje...")) {
						
						try {
							
							PaqueteDatos datos = new PaqueteDatos();
							datos.setNMatr(usuarioActual);
							datos.setMensaje(nombreUsuarioActual + ": " + txtEscribirMensaje.getText());
							datos.setNombre(lblNombre.getText());
							datos.setIdOperacion(4);
							
							Socket socket_cliente = new Socket(ip, puertoservidor);
							
							ObjectOutputStream flujo_s = new ObjectOutputStream(socket_cliente.getOutputStream());
							
							flujo_s.writeObject(datos);
							
							socket_cliente.close();			  //Por ultimo cerramos el socket para que no se envie nada mas
							
							String linea = nombreUsuarioActual + ": " + txtEscribirMensaje.getText() + "\n";
							textAreaChat.append(linea);
							
							txtEscribirMensaje.setText("Escribir mensaje...");
							
						} catch(Exception ex) {
							System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
						}
						
						txtEscribirMensaje.setText("Escribir mensaje...");
						txtEscribirMensaje.select(0, 0);
					}
				} else if (e.getKeyCode() != KeyEvent.VK_ENTER && txtEscribirMensaje.getText().equals("Escribir mensaje...")){
					txtEscribirMensaje.setText(null);
				}
			}
		});
		txtEscribirMensaje.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(txtEscribirMensaje.getText().equals("Escribir mensaje...") || txtEscribirMensaje.getText().equals("")) {
					txtEscribirMensaje.setText(null);
				}
			}
		});
		txtEscribirMensaje.setText("Escribir mensaje...");
		txtEscribirMensaje.setForeground(SystemColor.textInactiveText);
		txtEscribirMensaje.setColumns(10);
		add(txtEscribirMensaje);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(332, 238, 88, 29);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!txtEscribirMensaje.getText().equals("") && !txtEscribirMensaje.getText().equals("Escribir mensaje...")) {
					
					try {
						
						PaqueteDatos datos = new PaqueteDatos();
						datos.setNMatr(usuarioActual);
						datos.setMensaje(nombreUsuarioActual + ": " + txtEscribirMensaje.getText());
						datos.setNombre(lblNombre.getText());
						datos.setIdOperacion(4);
						
						Socket socket_cliente = new Socket(ip, puertoservidor);
						
						ObjectOutputStream flujo_s = new ObjectOutputStream(socket_cliente.getOutputStream());
						
						flujo_s.writeObject(datos);
						
						socket_cliente.close();			  //Por ultimo cerramos el socket para que no se envie nada mas
						
						String linea = nombreUsuarioActual + ": " + txtEscribirMensaje.getText() + "\n";
						textAreaChat.append(linea);
						
						txtEscribirMensaje.setText("Escribir mensaje...");
						
					} catch(Exception ex) {
						System.out.println("ERROR en actionPerformed del Cliente: " + ex.getMessage());
					}
					
				}
				txtEscribirMensaje.setText("Escribir mensaje...");
			}
		});
		btnEnviar.setForeground(Color.BLACK);
		btnEnviar.setBackground(SystemColor.activeCaption);
		add(btnEnviar);
		
	}
	
	
}
