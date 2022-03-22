package interlibros.interlibros.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import interlibros.interlibros.entidades.Intercambio;

@Repository
public interface IntercambioRepositorio extends JpaRepository<Intercambio, String> {

	@Query("SELECT c FROM Intercambio c WHERE c.usuario1.id = :id AND c.solicitado IS TRUE")
	public List<Intercambio> consultarIntercambiosEnviados(@Param("id") String id);
	
	@Query("SELECT c FROM Intercambio c WHERE c.usuario2.id = :id AND c.solicitado IS TRUE")
	public List<Intercambio> consultarIntercambiosRecibidos(@Param("id") String id);
	
	@Query("SELECT c FROM Intercambio c WHERE c.libro1.id = :idLibro OR c.libro2.id = :idLibro AND c.solicitado IS TRUE")
	public List<Intercambio> consultarIntercambioPorLibro(@Param("idLibro")String id);
	
}
