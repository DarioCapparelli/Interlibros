package interlibros.interlibros.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import interlibros.interlibros.entidades.Foto;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio implements UserDetailsService{
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private FotoServicio fotoServicio;
	
	@Transactional
	public void generarUsuario(MultipartFile archivo,String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
		
		validarUsuario(nombre, apellido, mail, clave, clave2);
		
		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setApellido(apellido);
		usuario.setMail(mail);
		String encriptada = new BCryptPasswordEncoder().encode(clave);
		usuario.setClave(encriptada);
		usuario.setAlta(new Date());
		
		Foto foto = fotoServicio.guardarFoto(archivo);
		usuario.setFoto(foto);
		
		usuarioRepositorio.save(usuario);
		
	}
	
	@Transactional
	public void modificarUsuario(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
		
		validarUsuario(nombre, apellido, mail, clave, clave2);
		
		Optional <Usuario> respuesta = usuarioRepositorio.findById(id);
		if(respuesta.isPresent()) {
			Usuario usuario = respuesta.get();
			if(usuario.getBaja() == null) {
				usuario.setNombre(nombre);
				usuario.setApellido(apellido);
				usuario.setMail(mail);
				
				Foto foto = fotoServicio.actualizarFoto(usuario.getFoto().getId(), archivo);
				usuario.setFoto(foto);
				
				usuarioRepositorio.save(usuario);
			}else {
				throw new ErrorServicio("El usuario debe estar activo para ser editado. Habilite su usuario para continuar con la edicion.");
			}			
		}else {
			throw new ErrorServicio("No se encontro al usuario solicitado");
		}
		
	}
	
	@Transactional
	public void deshabilitarUsuario(String id) throws ErrorServicio{
		
		Optional <Usuario> respuesta = usuarioRepositorio.findById(id);
		if(respuesta.isPresent()) {
			Usuario usuario = respuesta.get();
			usuario.setBaja(new Date());
			usuarioRepositorio.save(usuario);		
		}else {
			throw new ErrorServicio("No se encontro al usuario solicitado");
		}
	}
	
	@Transactional
	public void habilitarUsuario(String id) throws ErrorServicio{
		
		Optional <Usuario> respuesta = usuarioRepositorio.findById(id);
		if(respuesta.isPresent()) {
			Usuario usuario = respuesta.get();
			usuario.setBaja(null);
			usuarioRepositorio.save(usuario);		
		}else {
			throw new ErrorServicio("No se encontro al usuario solicitado");
		}
	}
	
	public void validarUsuario(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio{
		
		if(nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El nombre del usuario no puede ser nulo");
		}
		if(apellido == null || apellido.isEmpty()) {
			throw new ErrorServicio("El apellido del usuario no puede ser nulo");
		}
		if(mail == null || mail.isEmpty()) {
			throw new ErrorServicio("El mail del usuario no puede ser nulo");
		}
		if(clave == null || clave.length() < 6) {
			throw new ErrorServicio("La clave del usuario no puede ser nula o tener menos de 6 caracteres.");
		}
		if(!clave.equals(clave2)) {
			throw new ErrorServicio("Las claves deben coincidir.");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepositorio.buscarUsuarioPorMail(mail);
		if(usuario != null) {
			List<GrantedAuthority> permisos = new ArrayList<>();
			
			GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
			permisos.add(p1);
			
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuariosession", usuario);
			
			User user = new User(usuario.getMail(), usuario.getClave(), permisos);
			return user;
		}else {
			return null;
		}
	}	

}
