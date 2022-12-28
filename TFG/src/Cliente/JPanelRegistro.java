package Cliente;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JPanelRegistro extends JPanel {
	
	JTextField textFieldNombre;
	JTextField textFieldNMatr;
	JTextField textFieldPwd;
	JButton btnRegistro;

	/**
	 * Create the panel.
	 */
	public JPanelRegistro() {
		setLayout(null);
		
		JLabel lblRegistroDeUsuario = new JLabel("Registro de Usuario");
		lblRegistroDeUsuario.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblRegistroDeUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegistroDeUsuario.setBounds(115, 5, 218, 30);
		add(lblRegistroDeUsuario);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(10, 90, 55, 14);
		add(lblNombre);
		
		JLabel lblNmeroDeMatrcula = new JLabel("N\u00BA Matr\u00EDcula:");
		lblNmeroDeMatrcula.setBounds(10, 125, 73, 14);
		add(lblNmeroDeMatrcula);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(10, 160, 73, 14);
		add(lblContrasea);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(93, 87, 107, 20);
		add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		textFieldNMatr = new JTextField();
		textFieldNMatr.setBounds(93, 122, 107, 20);
		add(textFieldNMatr);
		textFieldNMatr.setColumns(10);
		
		textFieldPwd = new JTextField();
		textFieldPwd.setBounds(93, 157, 107, 20);
		add(textFieldPwd);
		textFieldPwd.setColumns(10);
		
		JLabel lblIconodeRegistro = new JLabel("");
		lblIconodeRegistro.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Fotos dise\u00F1o app\\iconoderegistro.png"));
		lblIconodeRegistro.setBounds(264, 42, 128, 181);
		add(lblIconodeRegistro);
		
		btnRegistro = new JButton("");
		btnRegistro.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Fotos dise\u00F1o app\\botonderegistro.png"));
		btnRegistro.setBounds(165, 220, 97, 51);
		add(btnRegistro);

	}
}
