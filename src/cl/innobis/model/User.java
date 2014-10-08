package cl.innobis.model;

import com.j256.ormlite.field.DatabaseField;

//Modelo del usuario, vamos a guardar su ID.
//Autor: Cristian Lehuede
public class User {
	@DatabaseField(id = true)
	int id;
	@DatabaseField
	String nombre;
	@DatabaseField
	String password;
	@DatabaseField
	String role;
	@DatabaseField
	String mail;

	@Override
	public String toString() {
		return "User [id=" + id + ", "
				+ (nombre != null ? "nombre=" + nombre + ", " : "")
				+ (password != null ? "password=" + password + ", " : "")
				+ (role != null ? "role=" + role + ", " : "")
				+ (mail != null ? "mail=" + mail : "") + "]";
	}

	public User() {

	}

	public User(int id, String nombre, String password, String header,
			String mail) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.role = header;
		this.mail = mail;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String header) {
		this.role = header;
	}

	public String getMail() {
		return mail;
	}

}
