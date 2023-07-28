package crud.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import crud.model.Produto;
import crud.repository.ProdutoRepository;

@Service
public class ProdutoService {
    
    ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository){
        this.repository = repository;
    }

    public void save(Produto produto){
        this.repository.save(produto);
    }

    public void delete(int id){
        Optional<Produto> produto = this.repository.findById(id);
        if(produto.isPresent())
            this.repository.delete(produto.get());
        else
            throw new EntityNotFoundException("Produto não existe");
    }

    public Produto findById(int id){
        Optional<Produto> produto = this.repository.findById(id);
        if(produto.isPresent())
            return produto.get();
        else
            throw new EntityNotFoundException("Produto não existe");
    }

    public List<Produto> findAll(){
        return this.repository.findAll();
    }

    public List<Produto> findByDescricao(String descricao){
        return this.repository.findByDescricao(descricao);
    }
}
