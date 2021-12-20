package crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import crud.Usuarios.Usuario;

public interface UsuarioRopository extends JpaRepository<Usuario, Integer>{
    
    @Query("Select u from Usuario u where u.nome like %:nome%")
    List<Usuario> findByNome(@Param("nome") String nome);

}