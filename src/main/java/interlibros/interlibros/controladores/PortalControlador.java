package interlibros.interlibros.controladores;

import java.util.List;

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
import interlibros.interlibros.servicios.UsuarioServicio;

@Controller
@RequestMapping("/")
public class PortalControlador {
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	@GetMapping("/bootstrap")
	public String bootstrap() {
		return "bootstrap.html";
	}
	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	@GetMapping("/exito")
	public String exito() {
		return "exito.html";
	}
	
	@GetMapping("/navegador")
	public String navegador(ModelMap modelo, HttpSession session) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null) {
			List<Libro> libros = libroRepositorio.consultarTodosLibros();
			modelo.addAttribute("libros",libros);
		}else {
			List<Libro> libros = libroRepositorio.consultarLibrosAjenos(login.getId());
			modelo.addAttribute("libros",libros);
		}

		return "navegador.html";
	}
	
	@PostMapping("/buscarLibro")
	public String buscarLibro(ModelMap modelo, HttpSession session, String busqueda) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		
		if(login == null) {
			List<Libro> libros = libroRepositorio.busquedaTodosLibros(busqueda);
			modelo.addAttribute("libros",libros);
		}else {
			List<Libro> libros = libroRepositorio.busquedaLibrosAjenos(busqueda, login.getId());
			modelo.addAttribute("libros",libros);
		}

		return "BuscarLibro.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
	@GetMapping("/inicio")
	public String inicio(){
		return "inicio.html";
	}
	
	/// LOGIN	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
		if(error != null) {
			model.put("error", "Nombre de usuario o clave incorrectos");
		}
		if(logout != null) {
			model.put("logout", "Has salido de la plataforma");
		}
		return "login.html";
	}
	
	/// REGISTRO	
	@GetMapping("/registro")
	public String registro(ModelMap modelo) {
		return "registro.html";
	}
	
	@PostMapping("/registrar")
	public String registrar(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {

		 try {
			usuarioServicio.generarUsuario(archivo, nombre, apellido, mail, clave1, clave2);
			modelo.put("titulo", "Registro de usuario");
			modelo.put("texto", "El usuario ha sido registrado de manera satisfactoria.");
			return "exito.html";
			
		} catch (ErrorServicio e) {;
			modelo.put("error", e.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("mail", mail);
			modelo.put("clave1", clave1); 
			modelo.put("clave2", clave2);
			e.printStackTrace();
			return "registro.html";
		}

	}
}
