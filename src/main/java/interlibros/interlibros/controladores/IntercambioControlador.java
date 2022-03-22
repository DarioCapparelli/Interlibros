package interlibros.interlibros.controladores;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import interlibros.interlibros.entidades.Intercambio;
import interlibros.interlibros.entidades.Libro;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.IntercambioRepositorio;
import interlibros.interlibros.repositorios.LibroRepositorio;
import interlibros.interlibros.repositorios.UsuarioRepositorio;
import interlibros.interlibros.servicios.IntercambioServicio;

@Controller
@RequestMapping("/intercambio")
public class IntercambioControlador {
	
	@Autowired
	private IntercambioRepositorio intercambioRepositorio;
        
    @Autowired
	private IntercambioServicio intercambioServicio;
        
    @Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @RequestMapping(value="/intercambio-solicitar", method = RequestMethod.GET)
    public String intercambioSolicitar(ModelMap modelo, HttpSession session, @RequestParam String idLibro2){
        
    	Libro libro2 = libroRepositorio.findById(idLibro2).get();
    	Usuario usuario2 = libro2.getUsuario();
        Usuario usuario1 = (Usuario) session.getAttribute("usuariosession");   
            
        try {
			intercambioServicio.solicitarIntercambio(usuario1, usuario2, libro2);
			modelo.put("titulo", "Solicitud de Intercambio");
			modelo.put("texto", "La solicitud de intercambio ha sido enviada de manera satisfactoria.");
			return "exito.html";
			
	    }catch(ErrorServicio e){
		    e.printStackTrace();
		    modelo.put("titulo", "Solicitud de Intercambio");
		    modelo.put("texto", e.getMessage());
		    return "exito.html";
        }                        
    }
    
    /// ACEPTAR INTERCAMBIO
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @RequestMapping(value="/intercambio-aceptar", method = RequestMethod.GET)
    public String intercambioAceptar(ModelMap modelo, HttpSession session, String idIntercambio, String idLibro){
        
    	Intercambio intercambio = intercambioRepositorio.getById(idIntercambio);
    	
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login == null) {
            return "redirect:/login";
        }else if (intercambio.getUsuario1().getId().equals(login.getId())) {  
        	modelo.put("titulo", "Solicitud de Intercambio");
            modelo.put("texto", "Acceso denegado. El intercambio continua pendiente de ser aceptado.");
            return "exito.html";      
        }
        
        Libro libro1 = libroRepositorio.getById(idLibro);
        
        try {
			intercambioServicio.aceptarIntercambio(idIntercambio, libro1);
	        modelo.put("titulo", "Solicitud de Intercambio");
	        modelo.put("texto", "El intercambio ha sido aceptado de manera satisfactoria. Revisa en la seccion de intercambios el contacto por medio del cual comunicarte para acordar la entrega");
	        return "exito.html";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
        	modelo.put("titulo", "Solicitud de Intercambio");
            modelo.put("texto", "El libro no se encuentra disponible.");
			return "exito.html";
		}
    }
    
    /// FINALIZAR INTERCAMBIO
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @RequestMapping(value="/intercambio-finalizar", method = RequestMethod.GET)
    public String intercambioFinalizar(ModelMap modelo, HttpSession session, String idIntercambio){

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login == null) {
            return "redirect:/login";
        }

        Intercambio intercambio = intercambioRepositorio.findById(idIntercambio).get();
       	if(!(intercambio.usuario1.getId().equals(login.getId()) || intercambio.usuario2.getId().equals(login.getId()))) {
            return "redirect:/login";
        }
        
        intercambioServicio.finalizarIntercambio(idIntercambio);
        modelo.put("titulo", "Solicitud de Intercambio");
        modelo.put("texto", "El intercambio ha sido finalizado de manera satisfactoria.");
        return "exito.html";
        }    
        
}