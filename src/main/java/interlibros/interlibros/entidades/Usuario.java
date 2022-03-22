package interlibros.interlibros.entidades;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String id;
	public String nombre;
	public String apellido;
	public String mail;
	public String clave;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date alta;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date baja;
	
	@OneToOne
	protected Foto foto;
	
	//public String apodo;
	//public Zona zona;
	
	public Usuario() {

	}

	public Usuario(String id, String nombre, String apellido, String mail, String clave, Date alta, Date baja, Foto foto) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.mail = mail;
		this.clave = clave;
		this.alta = alta;
		this.baja = baja;
		this.foto = foto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getAlta() {
		return alta;
	}

	public void setAlta(Date alta) {
		this.alta = alta;
	}

	public Date getBaja() {
		return baja;
	}

	public void setBaja(Date baja) {
		this.baja = baja;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}
	
	
	
}
