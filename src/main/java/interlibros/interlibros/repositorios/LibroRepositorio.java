package interlibros.interlibros.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import interlibros.interlibros.entidades.Libro;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {
	
	@Query("SELECT c FROM Libro c WHERE c.usuario.id = :id AND c.baja IS NULL")
	public List<Libro> consultarLibros(@Param("id") String id);
	
	@Query("SELECT c FROM Libro c WHERE c.usuario.id = :id AND c.baja IS NULL AND c.disponible IS TRUE")
	public List<Libro> consultarLibrosDisponibles(@Param("id") String id);
	
	@Query("SELECT c FROM Libro c WHERE c.baja IS NULL")
	public List<Libro> consultarTodosLibros();
	
	@Query("SELECT c FROM Libro c WHERE c.usuario.id != :id AND c.baja IS NULL")
	public List<Libro> consultarLibrosAjenos(@Param("id") String id);
	
	@Query("SELECT c FROM Libro c WHERE c.baja IS NULL AND c.titulo LIKE %:titulo% OR c.autor LIKE %:titulo%")
	public List<Libro> busquedaTodosLibros(@Param("titulo")String busqueda);
	
	@Query("SELECT c FROM Libro c WHERE c.usuario.id != :id AND c.baja IS NULL AND c.titulo LIKE %:titulo% OR c.autor LIKE %:titulo%")
	public List<Libro> busquedaLibrosAjenos(@Param("titulo")String busqueda, @Param("id") String id);
}
