package interlibros.interlibros.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Libro {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String id;
	public String titulo;
	public String autor;
	public String anio;
	public String descripcion;
	public boolean disponible;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date alta;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date baja;
	
	@OneToOne
	public Usuario usuario;
	
	@OneToOne
	public Foto foto;
	
	public Libro() {
	}

	public Libro(String id, String titulo, String autor, String anio, String descripcion, boolean disponible, Date alta, Date baja, Usuario usuario, Foto foto) {
		this.id = id;
		this.titulo = titulo;
		this.anio = anio;
		this.autor = autor;
		this.descripcion = descripcion;
		this.disponible = disponible;
		this.alta = alta;
		this.baja = baja;
		this.usuario = usuario;
		this.foto = foto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}
}
