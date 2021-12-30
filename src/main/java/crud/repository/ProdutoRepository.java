package crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import crud.Produtos.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
    @Query("select u from Produto u where u.descricao like %:descricao%")
    List<Produto> findByDescricao(@Param("descricao") String descricao);

}
