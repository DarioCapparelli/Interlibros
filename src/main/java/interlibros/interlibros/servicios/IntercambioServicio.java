package interlibros.interlibros.servicios;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interlibros.interlibros.entidades.Intercambio;
import interlibros.interlibros.entidades.Libro;
import interlibros.interlibros.entidades.Usuario;
import interlibros.interlibros.errores.ErrorServicio;
import interlibros.interlibros.repositorios.IntercambioRepositorio;
import interlibros.interlibros.repositorios.LibroRepositorio;

@Service
public class IntercambioServicio {
	
	@Autowired
	private IntercambioRepositorio intercambioRepositorio;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	@Transactional
	public void solicitarIntercambio(Usuario usuario1, Usuario usuario2, Libro libro2) throws ErrorServicio {
		
            validarSolicitudIntercambio(usuario1, usuario2, libro2);
    		
            Intercambio intercambio = new Intercambio();
            intercambio.setUsuario1(usuario1);
            intercambio.setUsuario2(usuario2);
            intercambio.setLibro2(libro2);
            intercambio.setSolicitado(true);
            intercambio.setAceptado(false);
		
            intercambioRepositorio.save(intercambio);

	}
	
	public void aceptarIntercambio(String idIntercambio, Libro libro1) throws ErrorServicio{
		
            Intercambio intercambio = intercambioRepositorio.findById(idIntercambio).get();
            //String idLibro1 = libro1.getId();
           // String idLibro2 = intercambio.libro2.getId();
            //validarAceptacionIntercambio(intercambio, idLibro1, idLibro2);
            
            if(libro1.isDisponible()){
            	
                intercambio.setLibro1(libro1);
                
                intercambio.setAlta(new Date());
                /*Calendar altaCalendar = new GregorianCalendar();
                altaCalendar.setTime(intercambio.getAlta());
                intercambio.setAltaString(altaCalendar.toString());*/
                
                intercambio.setBaja(setearBaja(intercambio.getAlta()));
               /* Calendar bajaCalendar = new GregorianCalendar();
                altaCalendar.setTime(intercambio.getBaja());
                intercambio.setBajaString(bajaCalendar.toString());*/
                
                intercambio.setAceptado(true);
    		
                intercambioRepositorio.save(intercambio);
                
                libro1.setDisponible(false);
                Libro libro2 = intercambio.getLibro2();
                libro2.setDisponible(false);
                
                libroRepositorio.save(libro1);
                libroRepositorio.save(libro2);
                
            }else {
            	throw new ErrorServicio("El libro no se encuentra disponible.");
            }
	}
	
	@Transactional
	public void finalizarIntercambio(String idIntercambio){
		
            Intercambio intercambio = intercambioRepositorio.findById(idIntercambio).get();
		
            intercambio.setSolicitado(false);
            intercambio.setAceptado(false);
		
            intercambioRepositorio.save(intercambio);          
            
            if(intercambio.getLibro1() != null) {
            	Libro libro1 = intercambio.getLibro1();
            	libro1.setDisponible(true);
                libroRepositorio.save(libro1);
            }
            	
            if(intercambio.getLibro2() != null) {
            	Libro libro2 = intercambio.getLibro2();
            	libro2.setDisponible(true);
                libroRepositorio.save(libro2);
            }
	}
        
    public void validarSolicitudIntercambio(Usuario usuario1, Usuario usuario2, Libro libro2) throws ErrorServicio{
        
    	List<Intercambio> intercambiosLibro = intercambioRepositorio.consultarIntercambioPorLibro(libro2.getId());
    	
        if(usuario1.getId().equals(usuario2.getId())){
            throw new ErrorServicio ("No podes solicitarte un prestamo a vos mismo");
        }
        
        for( Intercambio intercambio : intercambiosLibro) {
        	if(intercambio.getUsuario1().getId().equals(usuario1.getId()) || intercambio.getUsuario1().getId().equals(usuario2.getId()) || intercambio.getUsuario2().getId().equals(usuario1.getId()) || intercambio.getUsuario2().getId().equals(usuario2.getId())) {
        		 throw new ErrorServicio ("Ya existe una solicitud pendiente por este libro.");
        	}
        }     
    }
    
    public void validarAceptacionIntercambio(Intercambio intercambio, String idLibro1, String idLibro2) throws ErrorServicio{
    	
        if(((intercambio.getLibro1().getId().equals(idLibro1) || (intercambio.getLibro1().getId().equals(idLibro2)) && (intercambio.getLibro2().getId().equals(idLibro1) || (intercambio.getLibro2().getId().equals(idLibro2)))))) {
        	throw new ErrorServicio ("Ya has realizado una solicitud de este libro");
        }
                    
    }
    
    //Intercambio intercambio1 = intercambioRepositorio.consultarIntercambio1();
    

    
    public Date setearBaja(Date alta) {    
    	
    	Calendar bajaCalendar = new GregorianCalendar();
    	bajaCalendar.setTime(alta);
    	bajaCalendar.add(Calendar.MONTH, 1);
    	
    	Date baja = bajaCalendar.getTime();
    	
    	return baja;
    }

}