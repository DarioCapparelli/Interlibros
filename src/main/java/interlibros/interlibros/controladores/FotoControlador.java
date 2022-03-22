package interlibros.interlibros.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import interlibros.interlibros.entidades.Libro;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.LibroRepositorio;
import interlibros.interlibros.repositorios.UsuarioRepositorio;

@Controller
@RequestMapping("/foto")
public class FotoControlador {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {

        try {
            Usuario usuario = usuarioRepositorio.findById(id).get();
            if (usuario.getFoto() == null) {
                throw new ErrorServicio("El usuario no tiene una foto asignada.");
            }
            byte[] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
	
	
	@GetMapping("/libro/{id}")
	public ResponseEntity<byte[]> fotoLibro(@PathVariable String id) {
		
		try {
			Libro libro = libroRepositorio.getById(id);
			
			if(libro.getFoto() == null) {
				throw new ErrorServicio("Sin foto asignada");
			}
			
			byte[] foto = libro.getFoto().getContenido();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			
			return new ResponseEntity<>(foto, headers, HttpStatus.OK);
		}catch(ErrorServicio e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}





