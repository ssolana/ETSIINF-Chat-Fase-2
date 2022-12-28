package Cliente;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JPanelInicioSesion extends JPanel {
	JTextField textFieldNMatr;
	JButton btnInicioSesion;
	JPasswordField textPwd;

	/**
	 * Create the panel.
	 */
	public JPanelInicioSesion() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				char[] c = textPwd.getPassword();
				String pwd = new String(c);
				if(textFieldNMatr.getText().equals("") || pwd.equals("")) {
					textFieldNMatr.setText("Ingrese su número de matrícula...");
					textPwd.setText("contrase\u00F1a1234");
				}
			}
		});
		setLayout(null);
		
		JLabel lblInicioDeSesin = new JLabel("Inicio de Sesi\u00F3n");
		lblInicioDeSesin.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblInicioDeSesin.setHorizontalAlignment(SwingConstants.CENTER);
		lblInicioDeSesin.setBounds(115, 5, 218, 30);
		add(lblInicioDeSesin);
		
		JLabel lblNMatrcula = new JLabel("N\u00BA Matr\u00EDcula:");
		lblNMatrcula.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNMatrcula.setBounds(52, 70, 95, 38);
		add(lblNMatrcula);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblContrasea.setBounds(52, 125, 95, 38);
		add(lblContrasea);
		
		textFieldNMatr = new JTextField();
		textFieldNMatr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(textFieldNMatr.getText().equals("Ingrese su número de matrícula...")) {
					textFieldNMatr.setText(null);
				}
			}
		});
		textFieldNMatr.setFont(new Font("Tahoma", Font.ITALIC, 11));
		textFieldNMatr.setText("Ingrese su n\u00FAmero de matr\u00EDcula...");
		textFieldNMatr.setBounds(159, 80, 120, 24);
		add(textFieldNMatr);
		textFieldNMatr.setColumns(10);
		
		textPwd = new JPasswordField();
		textPwd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPwd.setText(null);
			}
		});
		textPwd.setText("contrase\u00F1a1234");
		textPwd.setName("");
		textPwd.setToolTipText("");
		textPwd.setBounds(159, 135, 120, 24);
		add(textPwd);
		
		JLabel lblIconoInicioSesion = new JLabel("");
		lblIconoInicioSesion.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Fotos dise\u00F1o app\\iconodeiniciarsesion.png"));
		lblIconoInicioSesion.setBounds(292, 61, 136, 140);
		add(lblIconoInicioSesion);
		
		btnInicioSesion = new JButton("");
		btnInicioSesion.setIcon(new ImageIcon("C:\\Users\\Sergio\\Desktop\\Uni\\4\u00BA\\TFG\\Fotos dise\u00F1o app\\botondeiniciodesesion.png"));
		btnInicioSesion.setBounds(159, 190, 95, 38);
		add(btnInicioSesion);

	}
}
