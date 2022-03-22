package interlibros.interlibros.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Intercambio {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String id;	
	
	@ManyToOne
	public Usuario usuario1;	
	
	@ManyToOne
	public Usuario usuario2;
	
	@ManyToOne
	public Libro libro1;
	
	@ManyToOne
	public Libro libro2;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date alta;
	public String altaString;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date baja;
	public String bajaString;
	
	public boolean solicitado;
	public boolean aceptado;
	
	public Intercambio() {
	}

	public Intercambio(String id, Usuario usuario1, Usuario usuario2, Libro libro1, Libro libro2, Date alta, Date baja, boolean solicitado, boolean aceptado, String altaString, String bajaString) {
		this.id = id;
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
		this.libro1 = libro1;
		this.libro2 = libro2;
		this.alta = alta;
		this.baja = baja;
		this.solicitado = solicitado;
		this.aceptado = aceptado;
		this.altaString = altaString;
		this.bajaString = bajaString;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Usuario getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}

	public Usuario getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}

	public Libro getLibro1() {
		return libro1;
	}

	public void setLibro1(Libro libro1) {
		this.libro1 = libro1;
	}

	public Libro getLibro2() {
		return libro2;
	}

	public void setLibro2(Libro libro2) {
		this.libro2 = libro2;
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

	public boolean isSolicitado() {
		return solicitado;
	}

	public void setSolicitado(boolean solicitado) {
		this.solicitado = solicitado;
	}

	public boolean isAceptado() {
		return aceptado;
	}

	public void setAceptado(boolean aceptado) {
		this.aceptado = aceptado;
	}

	public String getAltaString() {
		return altaString;
	}

	public void setAltaString(String altaString) {
		this.altaString = altaString;
	}

	public String getBajaString() {
		return bajaString;
	}

	public void setBajaString(String bajaString) {
		this.bajaString = bajaString;
	}
	
}
