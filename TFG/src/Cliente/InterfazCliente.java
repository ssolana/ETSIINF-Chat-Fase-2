package Cliente;
import Datos.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Panel;
import java.awt.FlowLayout;
import java.awt.CardLayout;


@SuppressWarnings("serial")
public class InterfazCliente extends JFrame implements Runnable {

	private JPanel contentPane;
	private JPanel panelInicial;
	ObtenerIP obtenerip = new ObtenerIP();
	JPanelChat chat = new JPanelChat();
	JPanelRegistro registro = new JPanelRegistro();
	JPanelInicioSesion inicioSesion = new JPanelInicioSesion();
	JPanelConversaciones conversaciones = new JPanelConversaciones();
	JPanelAgregarConversacion agregarConver = new JPanelAgregarConversacion();
	final int puertoservidor = 5001;
	final int puertocliente = 5002;
	JTextArea textAreaChat;
	private JPanel panelActual = contentPane;
	String usuarioActual;
	String nombreActual;
	ArrayList<String> amigos = new ArrayList<String>();
	public ArrayList<JButton> botones = new ArrayList<JButton>();
	String chatACargar;
	boolean ISCorrecto;
	boolean ExitoRegistro;
	boolean existeUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazCliente frame = new InterfazCliente();
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
	public InterfazCliente() {
		setTitle("ETSIINF - Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 464, 325);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		panelInicial = new JPanel();
		contentPane.add(panelInicial, "name_16714811886900");
		panelInicial.setLayout(null);
		
		JButton btnIniciarSesion = new JButton("Iniciar Sesi\u00F3n");
		btnIniciarSesion.setBounds(29, 124, 125, 23);
		panelInicial.add(btnIniciarSesion);
		
		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contentPane.removeAll();
				contentPane.add(registro);
				contentPane.repaint();
				contentPane.revalidate();
				panelActual = registro;
				registro.btnRegistro.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						try {
							
							PaqueteDatos datos = new PaqueteDatos();
							if(!registro.textFieldNMatr.getText().equals("") && !registro.textFieldNombre.getText().equals("")
									&& !registro.textFieldPwd.getText().equals("")) {
								datos.setIdOperacion(1);
								datos.setNMatr(registro.textFieldNMatr.getText());
								datos.setNombre(registro.textFieldNombre.getText());
								datos.setPwd(registro.textFieldPwd.getText());
								datos.setIPpublica(obtenerip.obtenerIPPublica());
								datos.setIPprivada(obtenerip.obtenerIPLocal());
								
								Socket socket_sr = new Socket("192.168.1.49", 5001);
								
								ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
								
								flujo_s.writeObject(datos);
								
								socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
							} else {
								JOptionPane.showMessageDialog(registro, "Por favor, rellene todos los campos", 
										"Error en el Registro", 0);
							}
							
						} catch(Exception ex) {
							System.out.println("ERROR en envio de Registro: " + ex.getMessage());
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException exc) {
							exc.printStackTrace();
						}
						if(ExitoRegistro == true) {
							chat.usuarioActual = usuarioActual;
							chat.nombreUsuarioActual = nombreActual;
							contentPane.removeAll();
							contentPane.add(conversaciones);
							
							for(int i=0;i<botones.size();i++) {
								conversaciones.panel.add(botones.get(i));
							}
							for(int i=0; i<botones.size();i++) {
								String nombre = botones.get(i).getText();
								botones.get(i).addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										contentPane.removeAll();
										contentPane.add(chat);
										chat.lblNombre.setText(nombre);
										cargarChat(nombre);
										contentPane.repaint();
										contentPane.revalidate();
										panelActual = chat;
									}
								});
							}
							conversaciones.btnAadir.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									contentPane.removeAll();
									contentPane.add(agregarConver);
									contentPane.repaint();
									contentPane.revalidate();
									panelActual = agregarConver;
								}
							});
							agregarConver.btnNuevoChat.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									
									if(!agregarConver.textFieldNMatr.getText().equals("") && 
											!agregarConver.textFieldNombre.getText().equals("")) {
										try {
											
											PaqueteDatos datos = new PaqueteDatos();
											datos.setIdOperacion(6);
											datos.setIdParticipante1(usuarioActual);
											datos.setNombreParticipante1(nombreActual);
											datos.setIdParticipante2(agregarConver.textFieldNMatr.getText());
											datos.setNombreParticipante2(agregarConver.textFieldNombre.getText());
											
											Socket socket_sr = new Socket("192.168.1.49", 5001);
											
											ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
											
											flujo_s.writeObject(datos);
											
											socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
											
											
										} catch(Exception ex) {
											System.out.println("ERROR en envio de agregarConversacion: " + ex.getMessage());
										}
										try {
											Thread.sleep(200);
										} catch (InterruptedException exc) {
											exc.printStackTrace();
										}
										if(existeUsuario == true) {
											JButton boton = new JButton(agregarConver.textFieldNombre.getText());
											conversaciones.panel.add(boton);
											contentPane.removeAll();
											contentPane.add(conversaciones);
											contentPane.repaint();
											contentPane.revalidate();
											panelActual = conversaciones;
											boton.addActionListener(new ActionListener() {
												public void actionPerformed(ActionEvent e) {
													contentPane.removeAll();
													contentPane.add(chat);
													chat.lblNombre.setText(boton.getText());
													cargarChat(boton.getText());
													contentPane.repaint();
													contentPane.revalidate();
													panelActual = chat;
												}
											});
										} else {
											JOptionPane.showMessageDialog(agregarConver, "Dicho usuario no existe", 
													"Error al crear una conversación", 0);
										}
									}
								}
							});
							contentPane.repaint();
							contentPane.revalidate();
							panelActual = conversaciones;
						} else {
							JOptionPane.showMessageDialog(registro, "Intentelo de nuevo", 
									"Error en el Registro", 0);
						}
					}
				});
				
				chat.btnVolver.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						contentPane.removeAll();
						contentPane.add(conversaciones);
						chat.textAreaChat.setText(null);
						contentPane.repaint();
						contentPane.revalidate();
						panelActual = conversaciones;
					}
				});
				
				agregarConver.btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						contentPane.removeAll();
						contentPane.add(conversaciones);
						contentPane.repaint();
						contentPane.revalidate();
						panelActual = conversaciones;
					}
				});
			}
		});
		btnRegistrarse.setBounds(29, 176, 125, 23);
		panelInicial.add(btnRegistrarse);
		
		JLabel imagenInicioSesion = new JLabel("");
		imagenInicioSesion.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Fotos dise\u00F1o app\\iniciosesionimg.png"));
		imagenInicioSesion.setBounds(178, 11, 250, 254);
		panelInicial.add(imagenInicioSesion);
		
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.removeAll();
				contentPane.add(inicioSesion);
				contentPane.repaint();
				contentPane.revalidate();
				panelActual = inicioSesion;
				inicioSesion.btnInicioSesion.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						try {
							
							PaqueteDatos datos = new PaqueteDatos();
							datos.setIdOperacion(2);
							datos.setNMatr(inicioSesion.textFieldNMatr.getText());
							char[] pass = inicioSesion.textPwd.getPassword();
							String pwd = new String(pass);
							datos.setPwd(pwd);
							
							Socket socket_sr = new Socket("192.168.1.49", 5001);
							
							ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
							
							flujo_s.writeObject(datos);
							
							socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
							
							
						} catch(Exception ex) {
							System.out.println("ERROR en envio de inicioSesion: " + ex.getMessage());
						}
						try {
							Thread.sleep(1800);
						} catch (InterruptedException exc) {
							exc.printStackTrace();
						}
						
						if(ISCorrecto == true) {
							chat.usuarioActual = usuarioActual;
							chat.nombreUsuarioActual = nombreActual;
							contentPane.removeAll();
							contentPane.add(conversaciones);
							solicitarConversaciones(usuarioActual);
							try {
								Thread.sleep(800);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							for(int i=0;i<botones.size();i++) {
								conversaciones.panel.add(botones.get(i));
							}
							for(int i=0; i<botones.size();i++) {
								String nombre = botones.get(i).getText();
								botones.get(i).addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										contentPane.removeAll();
										contentPane.add(chat);
										chat.lblNombre.setText(nombre);
										cargarChat(nombre);
										contentPane.repaint();
										contentPane.revalidate();
										panelActual = chat;
									}
								});
							}
							conversaciones.btnAadir.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									contentPane.removeAll();
									contentPane.add(agregarConver);
									contentPane.repaint();
									contentPane.revalidate();
									panelActual = agregarConver;
								}
							});
							agregarConver.btnNuevoChat.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									
									if(!agregarConver.textFieldNMatr.getText().equals("") && 
											!agregarConver.textFieldNombre.getText().equals("")) {
										try {
											
											PaqueteDatos datos = new PaqueteDatos();
											datos.setIdOperacion(6);
											datos.setIdParticipante1(usuarioActual);
											datos.setNombreParticipante1(nombreActual);
											datos.setIdParticipante2(agregarConver.textFieldNMatr.getText());
											datos.setNombreParticipante2(agregarConver.textFieldNombre.getText());
											
											Socket socket_sr = new Socket("192.168.1.49", 5001);
											
											ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
											
											flujo_s.writeObject(datos);
											
											socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
											
											
										} catch(Exception ex) {
											System.out.println("ERROR en envio de agregarConversacion: " + ex.getMessage());
										}
										try {
											Thread.sleep(200);
										} catch (InterruptedException exc) {
											exc.printStackTrace();
										}
										if(existeUsuario == true) {
											JButton boton = new JButton(agregarConver.textFieldNombre.getText());
											conversaciones.panel.add(boton);
											contentPane.removeAll();
											contentPane.add(conversaciones);
											contentPane.repaint();
											contentPane.revalidate();
											panelActual = conversaciones;
											boton.addActionListener(new ActionListener() {
												public void actionPerformed(ActionEvent e) {
													contentPane.removeAll();
													contentPane.add(chat);
													chat.lblNombre.setText(boton.getText());
													cargarChat(boton.getText());
													contentPane.repaint();
													contentPane.revalidate();
													panelActual = chat;
												}
											});
										} else {
											JOptionPane.showMessageDialog(agregarConver, "Dicho usuario no existe", 
													"Error al crear una conversación", 0);
										}
									}
								}
							});
							contentPane.repaint();
							contentPane.revalidate();
							panelActual = conversaciones;
						} else {
							JOptionPane.showMessageDialog(panelActual, "Los datos introducidos son incorrectos " + usuarioActual
									 + " " + ISCorrecto,
									"Error en Inicio de Sesión", 0);
							inicioSesion.textFieldNMatr.setText("Ingrese su número de matrícula...");
							inicioSesion.textPwd.setText("contrase\u00F1a1234");
						}
					}
				});
				
				chat.btnVolver.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						contentPane.removeAll();
						contentPane.add(conversaciones);
						chat.textAreaChat.setText(null);
						contentPane.repaint();
						contentPane.revalidate();
						panelActual = conversaciones;
					}
				});
				
				agregarConver.btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						contentPane.removeAll();
						contentPane.add(conversaciones);
						contentPane.repaint();
						contentPane.revalidate();
						panelActual = conversaciones;
					}
				});
			}

		});
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	
	@Override
	public void run() {
		
		try {
			
			ServerSocket socket_servidor = new ServerSocket(puertocliente);//Creo otro ServerSocket para que el cliente pueda estar
			
			while(true) {
			
				Socket socket_cl = socket_servidor.accept();
				
				ObjectInputStream flujo_e = new ObjectInputStream(socket_cl.getInputStream());
				
				PaqueteDatos info = (PaqueteDatos) flujo_e.readObject();
				
				if(info.getIdOperacion() == 1) {
					ExitoRegistro = info.getRegistro();
					usuarioActual = info.getNMatr();
					nombreActual = info.getNombre();
				} else if(info.getIdOperacion() == 2) {
					ISCorrecto = info.getIS();
					usuarioActual = info.getNMatr();
					nombreActual = info.getNombre();
				} else if(info.getIdOperacion() == 3) {
					amigos = info.getChatsAbiertos();
					for(int i=0; i<amigos.size();i++) {
						JButton boton = new JButton(amigos.get(i));
						botones.add(boton);
					}
				} else if(info.getIdOperacion() == 4) { //Operacion chat
					String mensaje = info.getMensaje();
					
					if(panelActual.equals(chat)) {
						String linea = mensaje + "\n";
						chat.textAreaChat.append(linea);			//Guardo el mensaje en el registro de mensajes, es decir, en la ventana
					} else if(panelActual.equals(conversaciones)) {
						String mensajeDe = info.getNombre();
						if(mensajeDe.equals("Servidor")) {
							Integer mensajeNL = 1;
							botones.get(1).setText("Maria" + " - " + mensajeNL.toString() + " mensaje no leido");
						}
					}
				} else if(info.getIdOperacion() == 5) {
					ArrayList<String> mensajes = info.getMensajes();
					
					for(int i=0;i<mensajes.size();i++) {
						chat.textAreaChat.append(mensajes.get(i) + "\n");
					}
					
				} else if(info.getIdOperacion() == 6) {
					existeUsuario = info.getExisteUsuario();
				}
				
				socket_cl.close();
				
			}
			
		} catch(Exception ex) {
			System.out.println("Error en run() del Cliente: " + ex.getMessage());
		}
		
	}
	
	
	public void cargarChat(String nombre) {
		
	    try {
			
			PaqueteDatos datos = new PaqueteDatos();
			datos.setIdOperacion(5);
			datos.setNombre(nombre);
			datos.setNMatr(usuarioActual);
			
			Socket socket_sr = new Socket("192.168.1.49", 5001);
			
			ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
			
			flujo_s.writeObject(datos);
			
			socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
			
			
		} catch(Exception ex) {
			System.out.println("ERROR en envio de cargarChat: " + ex.getMessage());
		}
	      	
	}
	
	public void solicitarConversaciones(String nmatr) {
		
		try {
			
			PaqueteDatos datos = new PaqueteDatos();
			datos.setIdOperacion(3);
			datos.setNMatr(nmatr);
			
			Socket socket_sr = new Socket("192.168.1.49", 5001);
			
			ObjectOutputStream flujo_s = new ObjectOutputStream(socket_sr.getOutputStream());
			
			flujo_s.writeObject(datos);
			
			socket_sr.close();			//Por ultimo cerramos el socket para que no se envie nada mas
			
			
		} catch(Exception ex) {
			System.out.println("ERROR en envio de conversaciones: " + ex.getMessage());
		}
	}
	
	
}
