package Cliente;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class JPanelAgregarConversacion extends JPanel {
	
	JTextField textFieldNombre;
	JTextField textFieldNMatr;
	JButton btnNuevoChat;
	JButton btnCancelar;

	/**
	 * Create the panel.
	 */
	public JPanelAgregarConversacion() {
		setLayout(null);
		
		JLabel lblAgregarConversacion = new JLabel("A\u00F1adir un nuevo chat");
		lblAgregarConversacion.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblAgregarConversacion.setHorizontalAlignment(SwingConstants.CENTER);
		lblAgregarConversacion.setBounds(115, 5, 218, 30);
		add(lblAgregarConversacion);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(100, 80, 55, 14);
		add(lblNombre);
		
		JLabel lblNmeroDeMatrcula = new JLabel("N\u00BA Matr\u00EDcula:");
		lblNmeroDeMatrcula.setBounds(82, 130, 73, 14);
		add(lblNmeroDeMatrcula);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(165, 77, 107, 20);
		add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		textFieldNMatr = new JTextField();
		textFieldNMatr.setBounds(165, 127, 107, 20);
		add(textFieldNMatr);
		textFieldNMatr.setColumns(10);
		
		btnNuevoChat = new JButton("A\u00F1adir Chat");
		btnNuevoChat.setBounds(165, 180, 97, 51);
		add(btnNuevoChat);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(165, 242, 99, 23);
		add(btnCancelar);
	}

}
