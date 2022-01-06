package crud.Controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import crud.Produtos.Produto;
import crud.repository.ProdutoRepository;

@Controller
@RequestMapping("/produto")
public class BuscaProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping("/busca")
	public String cadastro(ModelMap model, HttpSession sessao) {
		model.addAttribute("produto", new Produto());
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		return "busca";
	}
	
	@GetMapping("/buscar")
	public String buscar(@RequestParam(name="descricao", required=false) String descricao,
			             @RequestParam(name="mostrarTodosDados", required=false) Boolean mostrarTodosDados,
			             HttpSession sessao, ModelMap model) {
		
		List<Produto> produtosEncontrados = produtoRepository.findByDescricao(descricao);

		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("produtosEncontrados", produtosEncontrados);
		
		if(mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		return "busca";
	}
	
	@GetMapping("/estoque")
	public String estoque(ModelMap model, HttpSession sessao){

		List<Produto> produtosEncontrados = produtoRepository.findAll();
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("produtosEncontrados", produtosEncontrados);

		return "estoque";
	}
	
	@GetMapping("/remover/{id}")
	public String remover(@PathVariable("id") Integer idProduto, RedirectAttributes attr, HttpSession sessao) {
		
		produtoRepository.deleteById(idProduto);
		attr.addAttribute("msgSucesso", "Remoção bem sucedida");
		
		return "redirect:/produto/buscar";
	}
	
	@ModelAttribute("categorias")
	public List<String>  issae() {
	return Arrays.asList("Eletrônico", "Eletrodoméstico",
						"Alimento", "Prod. Limpeza", "Bebida", "Papelaria", "Higiene", "Outra");
	}
	
	
}
