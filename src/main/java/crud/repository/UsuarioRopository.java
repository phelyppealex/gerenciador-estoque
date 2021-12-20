package crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crud.Usuarios.Usuario;

public interface UsuarioRopository extends JpaRepository<Usuario, Integer>{
    
}
