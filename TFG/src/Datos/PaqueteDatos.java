package Datos;

import java.io.Serializable;
import java.util.ArrayList;

public class PaqueteDatos implements Serializable {
	
	String mensaje, IPpublica, IPprivada, nombre, NMatr, Pwd, idParticipante1, idParticipante2, nombreParticipante1, nombreParticipante2;
	int idOperacion;
	boolean IS, Registro, existeUsuario;
	ArrayList<String> chatsAbiertos;
	ArrayList<String> mensajes;
	
	public String getMensaje() {
		return this.mensaje;
	}
	
	public String getIPpublica() {
		return this.IPpublica;
	}
	
	public String getIPprivada() {
		return this.IPprivada;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getNMatr() {
		return this.NMatr;
	}
	
	public String getPwd() {
		return this.Pwd;
	}
	
	public String getIdParticipante1() {
		return this.idParticipante1;
	}
	
	public String getIdParticipante2() {
		return this.idParticipante2;
	}
	
	public String getNombreParticipante1() {
		return this.nombreParticipante1;
	}
	
	public String getNombreParticipante2() {
		return this.nombreParticipante2;
	}
	
	public int getIdOperacion() {
		return this.idOperacion;
	}
	
	public boolean getIS() {
		return this.IS;
	}
	
	public boolean getRegistro() {
		return this.Registro;
	}
	
	public boolean getExisteUsuario() {
		return this.existeUsuario;
	}
	
	public ArrayList<String> getChatsAbiertos() {
		return this.chatsAbiertos;
	}
	
	public ArrayList<String> getMensajes() {
		return this.mensajes;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public void setIPpublica(String ip) {
		this.IPpublica = ip;
	}
	
	public void setIPprivada(String ip) {
		this.IPprivada = ip;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setNMatr(String nmatr) {
		this.NMatr = nmatr;
	}
	
	public void setPwd(String pwd) {
		this.Pwd = pwd;
	}
	
	public void setIdParticipante1(String p1) {
		this.idParticipante1 = p1;
	}
	
	public void setIdParticipante2(String p2) {
		this.idParticipante2 = p2;
	}
	
	public void setNombreParticipante1(String n1) {
		this.nombreParticipante1 = n1;
	}
	
	public void setNombreParticipante2(String n2) {
		this.nombreParticipante2 = n2;
	}
	
	public void setIdOperacion(int op) {
		this.idOperacion = op;
	}
	
	public void setIS(boolean is) {
		this.IS = is;
	}
	
	public void setRegistro(boolean reg) {
		this.Registro = reg;
	}
	
	public void setExisteUsuario(boolean eu) {
		this.existeUsuario = eu;
	}
	
	public void setChatsAbiertos(ArrayList<String> chats) {
		this.chatsAbiertos = chats;
	}
	
	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}

}
