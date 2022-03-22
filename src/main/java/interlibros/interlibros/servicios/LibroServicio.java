package interlibros.interlibros.servicios;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import interlibros.interlibros.entidades.Foto;
import interlibros.interlibros.entidades.Libro;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.LibroRepositorio;
import interlibros.interlibros.repositorios.UsuarioRepositorio;

@Service
public class LibroServicio {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	@Autowired
	private FotoServicio fotoServicio;
	
	@Transactional
	public void generarLibro(MultipartFile archivo, String idUsuario, String titulo, String autor, String anio, String descripcion) throws ErrorServicio {
		
		Usuario usuario = usuarioRepositorio.findById(idUsuario).get();
		validarLibro(titulo, autor, anio, descripcion);
		
		Libro libro = new Libro();
		libro.setTitulo(titulo);
		libro.setAnio(anio);
		libro.setAutor(autor);
		libro.setDescripcion(descripcion);
		libro.setDisponible(true);
		libro.setAlta(new Date());
		libro.setUsuario(usuario);
		
		Foto foto = fotoServicio.guardarFoto(archivo);
		libro.setFoto(foto);
		
		libroRepositorio.save(libro);
	}
	
	@Transactional
	public void modificarLibro(MultipartFile archivo, String idLibro, String idUsuario, String titulo, String autor, String anio, String descripcion, boolean disponible) throws ErrorServicio {
		
		validarLibro(titulo,anio,autor, descripcion);
		
		Optional <Libro> respuesta = libroRepositorio.findById(idLibro);
		if(respuesta.isPresent()) {
			Libro libro = respuesta.get();
			
			if(libro.getUsuario().getId().equals(idUsuario)) {
				libro.setTitulo(titulo);
				libro.setAutor(autor);
				libro.setAnio(anio);
				libro.setDescripcion(descripcion);
				libro.setDisponible(disponible);
				
				Foto foto = fotoServicio.actualizarFoto(libro.getFoto().getId(), archivo);
				libro.setFoto(foto);
				
				libroRepositorio.save(libro);
				
			}else{
				throw new ErrorServicio("No tiene permisos suficientes para realizar la operación.");
			}			
			
		}else{
			throw new ErrorServicio("No existe un libro con el identificador solicitado.");
		}
		
	}
	
	@Transactional
	public void eliminarLibro(String idUsuario, String idLibro) throws ErrorServicio{
		
		Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
		if(respuesta.isPresent()) {
			Libro libro = respuesta.get();
			
			if(libro.isDisponible()) {
				
				if(libro.getUsuario().getId().equals(idUsuario)) {				
					libro.setBaja(new Date());		
					libroRepositorio.save(libro);
					
				}else{
					throw new ErrorServicio("No tiene permisos suficientes para realizar la operación.");
				}	
				
			}else{
				throw new ErrorServicio("No es posible eliminar un libreo que tiene un prestamo vigente. Aguarda a que concluya el prestamo o cancelalo, para asi poder eliminar el libro.");
			}
			
		}else{
			throw new ErrorServicio("No existe el libro con el identificador solicitado.");
		}
		
	}
	
	public void validarLibro(String titulo, String autor, String anio, String descripcion) throws ErrorServicio {
		
		if(titulo == null || titulo.isEmpty()) {
			throw new ErrorServicio("El titulo del libro no puede estar vacio");
		}
		if(autor == null || autor.isEmpty()) {
			throw new ErrorServicio("El titulo del libro no puede estar vacio");
		}
		if(anio == null || anio.isEmpty()){
			throw new ErrorServicio("El año de publicación del libro no puede estar vacio");
		}
		if(descripcion == null || descripcion.isEmpty()){
			throw new ErrorServicio("La descripcion del libro no puede estar vacio");
		}
	}

}
