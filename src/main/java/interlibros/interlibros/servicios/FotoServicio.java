package interlibros.interlibros.servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import interlibros.interlibros.entidades.Foto;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.FotoRepositorio;



@Service
public class FotoServicio {

	@Autowired
	private FotoRepositorio fotoRepository;
	
	@Transactional
	public Foto guardarFoto(MultipartFile archivo) throws ErrorServicio{
		if(archivo != null) {
			try {
				Foto foto = new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				return fotoRepository.save(foto);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				throw new ErrorServicio("No se pudo guardar la Foto");
			}
		}
		return null;
	}
	
	@Transactional
	public Foto actualizarFoto(String id, MultipartFile archivo) throws ErrorServicio{
		if(archivo != null) {
			try {
				Foto foto = new Foto();
				
				if(id != null) {
					Optional<Foto> respuesta = fotoRepository.findById(id);
					if(respuesta.isPresent()) {
						foto = respuesta.get();
					}
				}
				
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				return fotoRepository.save(foto);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				throw new ErrorServicio("No se pudo guardar la Foto");
			}
		}
		return null;
	}
	
	@Transactional
	public Foto eliminarFoto(String id) throws ErrorServicio{
		Optional<Foto> respuesta = fotoRepository.findById(id);
		if (respuesta.isPresent()) {
			Foto foto = respuesta.get();
			fotoRepository.delete(foto);
			return foto;
		}
		throw new ErrorServicio("La Foto no se encuentra en la base de datos");
	}
	
}