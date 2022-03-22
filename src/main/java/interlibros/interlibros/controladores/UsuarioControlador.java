package interlibros.interlibros.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import interlibros.interlibros.entidades.Intercambio;
import interlibros.interlibros.entidades.Libro;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.IntercambioRepositorio;
import interlibros.interlibros.repositorios.LibroRepositorio;
import interlibros.interlibros.repositorios.UsuarioRepositorio;
import interlibros.interlibros.servicios.UsuarioServicio;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	@Autowired
	private IntercambioRepositorio intercambioRepositorio;
	
	/// DESHABILITACIÓN	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@DeleteMapping("/usuario-deshabilitar")
	public String usuarioDeshabilitar(ModelMap modelo, HttpSession session) {
		
		Usuario login = (Usuario)session.getAttribute("usuariosession");
		try {
			usuarioServicio.deshabilitarUsuario(login.getId());

		}catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			modelo.put("idUsuario", login.getId());
			return "usuario-deshabilitacion.html";

		}
		modelo.put("texto", "Deshabilitacion exitosa");
		return "exito.html";
	}
	
	/// HABILITACIÓN	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/usuario-habilitacion")
	public String usuarioHabilitacion() {
		return "usuario-habilitacion.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@PostMapping("/usuario-habilitar")
	public String UsuarioHabilitar(ModelMap modelo, @RequestParam String idUsuario) {
		
		try {
			usuarioServicio.habilitarUsuario(idUsuario);

		}catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			modelo.put("idUsuario", idUsuario);
			return "usuario-habilitacion.html";

		}
		modelo.put("texto", "Habilitacion exitosa");
		return "exito.html";
	}
	
	/// EDICIÓN	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/usuario-edicion")
	public String usuarioEdicion(@RequestParam String id, ModelMap model) {
		
		Usuario usuario = usuarioRepositorio.getById(id);
		model.addAttribute("perfil", usuario);
		return "usuario-edicion.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@PostMapping("/usuario-editar")
	public String usuarioEditar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String nombre, @RequestParam String id, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
		
		try {
			Usuario usuario = usuarioRepositorio.getById(id);
			usuarioServicio.modificarUsuario(archivo, id, nombre, apellido, mail, clave1, clave2);
			session.setAttribute("usuariosession", usuario);
			return "exito.html";
		} catch (ErrorServicio e) {;
			modelo.put("error", e.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("mail", mail);
			e.printStackTrace();
			return "usuario-edicion.html";
		}
	}
	
	/// MOSTRAR LIBROS	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/usuario-libros")
	public String usuarioLibros(ModelMap modelo, HttpSession session) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null) {
			return "redirect:/login";
		}
		
		List<Libro> libros = libroRepositorio.consultarLibros(login.getId());
		modelo.addAttribute("libros",libros);
		
		return "usuario-libros.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/usuario-intercambios")
	public String usuarioIntercambios(ModelMap modelo, HttpSession session) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null) {
			return "redirect:/login";
		}
		
		List <Intercambio> intercambiosEnviados = intercambioRepositorio.consultarIntercambiosEnviados(login.getId());
		List <Intercambio> intercambiosRecibidos = intercambioRepositorio.consultarIntercambiosRecibidos(login.getId());
		
		modelo.addAttribute("intercambiosEnviados",intercambiosEnviados);
		modelo.addAttribute("intercambiosRecibidos",intercambiosRecibidos);
		
		return "usuario-intercambios.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@RequestMapping(value="/usuario-libros-intercambio", method = RequestMethod.GET)
	public String usuarioLibroIntercambios(ModelMap modelo, HttpSession session, String idIntercambio) {
		
		Intercambio intercambio = intercambioRepositorio.getById(idIntercambio);
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null) {
			return "redirect:/login";
		}else if (intercambio.getUsuario1().getId().equals(login.getId())) {
            return "index.html";      
        }
		
		List<Libro> libros = libroRepositorio.consultarLibrosDisponibles(intercambio.getUsuario1().getId());
		modelo.addAttribute("libros",libros);
		modelo.put("intercambio", intercambio);
		
		return "usuario-libros-intercambio.html";
	}

}
