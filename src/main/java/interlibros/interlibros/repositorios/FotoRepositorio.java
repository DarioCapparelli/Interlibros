package interlibros.interlibros.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import interlibros.interlibros.entidades.Foto;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {

}