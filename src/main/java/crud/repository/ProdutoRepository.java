package crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crud.Produtos.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
}
