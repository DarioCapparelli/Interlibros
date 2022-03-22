package interlibros.interlibros.controladores;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import interlibros.interlibros.entidades.Libro;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.LibroRepositorio;
import interlibros.interlibros.repositorios.UsuarioRepositorio;
import interlibros.interlibros.servicios.LibroServicio;

@Controller
@RequestMapping("/libro")
public class LibroControlador {
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	@Autowired
	private LibroServicio libroServicio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	/// REGISTRO
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/libro-registro")
	public String libroRegistro(@RequestParam String id, ModelMap model) {		
		Usuario usuario = usuarioRepositorio.getById(id);
		model.addAttribute("usuario", usuario);		
		return "libro-registro.html";
	}	

	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@PostMapping("/libro-registrar")
	public String libroRegistrar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String titulo, @RequestParam String autor, @RequestParam String anio, @RequestParam String descripcion) {

		 try {
			libroServicio.generarLibro(archivo, id, titulo, autor, anio, descripcion);
		} catch (ErrorServicio e) {;
			modelo.put("error", e.getMessage());
			modelo.put("titulo", titulo);
			modelo.put("autor", autor);
			modelo.put("anio", anio);
			modelo.put("descripcion", descripcion);			
			e.printStackTrace();
			return "libro-registro.html";
		}
		modelo.put("texto", "El libro ha sido registrado de manera satisfactoria");
		return "exito.html";
	}
	
	/// DESHABILITACIÓN	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/libro-deshabilitacion")
	public String libroDeshabilitacion(String id, ModelMap modelo) {
		
		Libro libro = libroRepositorio.getById(id);
		modelo.addAttribute("libro",libro);		
		return "libro-deshabilitacion.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@PostMapping("/libro-deshabilitar")
	public String libroDeshabilitar(ModelMap modelo, HttpSession session, @RequestParam String id) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null) {
			return "redirect:/login";
		}
		
		try {			
			libroServicio.eliminarLibro(login.getId(), id);
		}catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "libro-deshabilitacion.html";

		}
		modelo.put("texto", "El libro ha sido eliminado de manera satisfactoria");
		return "exito.html";	
	}
	
	/// EDICIÓN	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/libro-edicion")
	public String libroEdicion(String idLibro, ModelMap modelo) {
		
		Libro libro = libroRepositorio.getById(idLibro);
		modelo.addAttribute("libro",libro);	
		return "libro-edicion.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@PostMapping("/libro-editar")
	public String libroEditar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String idLibro, @RequestParam String idUsuario, @RequestParam String titulo, @RequestParam String autor, @RequestParam String anio, @RequestParam String descripcion, @RequestParam(required = false) boolean disponible) {

		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null) {
			return "redirect:/login";
		}
		
		 try {
			libroServicio.modificarLibro(archivo, idLibro, idUsuario, titulo, autor, anio, descripcion, disponible);

		} catch (ErrorServicio e) {
			Libro libro = libroRepositorio.getById(idLibro);
			modelo.addAttribute("libro",libro);	
			modelo.put("error", e.getMessage());
			e.printStackTrace();
			return "libro-edicion.html";
		}
		modelo.put("texto", "El libro ha sido editado de manera satisfactoria");
		return "exito.html";
	}
	


}
