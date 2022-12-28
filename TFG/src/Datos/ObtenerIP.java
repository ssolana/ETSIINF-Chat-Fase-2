package Datos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class ObtenerIP {

	public String obtenerIPLocal() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return ip;
	}
	
	public String obtenerIPPublica() {
		String ip = "";
		try {
			URL tempURL = new URL("http://checkip.amazonaws.com");
			BufferedReader tempBr = new BufferedReader(new InputStreamReader(tempURL.openStream()));
			String linea;
			while((linea = tempBr.readLine()) != null) {
				ip = linea;
			}
			tempBr.close();
		} catch(Exception ex) {
			System.out.println("No se ha podido obtener la dirección IP");
		}
		return ip;
	}
	
}
