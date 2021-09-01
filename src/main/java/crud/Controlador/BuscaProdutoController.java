package crud.Controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import crud.Produtos.Produto;

@Controller
@RequestMapping("/produto")
public class BuscaProdutoController {
	
	@GetMapping("/busca")
	public String cadastro(ModelMap model) {
		model.addAttribute("produto", new Produto());
		return "busca";
	}
	
	@GetMapping("/buscar")
	public String buscar(@RequestParam(name="descricao", required=false) String descricao,
			             @RequestParam(name="mostrarTodosDados", required=false) Boolean mostrarTodosDados,
			             HttpSession sessao, ModelMap model) {
		
		List<Produto> produtosCadastrados = (List<Produto>) sessao.getAttribute("produtosCadastrados");
		List<Produto> produtosEncontrados = new ArrayList<>();
		
		if((descricao != null || !descricao.isEmpty()) & produtosCadastrados != null){
			produtosEncontrados = (List<Produto>) produtosCadastrados.stream()
					.filter(
						u -> u.getDescricao().toLowerCase().contains(
							descricao.toLowerCase()
						)
					).collect(Collectors.toList());
		}
		
		model.addAttribute("produtosEncontrados", produtosEncontrados);
		
		if(mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		return "busca";
	}
	
	@GetMapping("/estoque")
	public String estoque(ModelMap model, HttpSession sessao){
		List<Produto> produtosCadastrados = (List<Produto>) sessao.getAttribute("produtosCadastrados");
		List<Produto> produtosEncontrados = new ArrayList<>();
		
		produtosEncontrados = produtosCadastrados;
		
		model.addAttribute("produtosEncontrados", produtosEncontrados);
		return "estoque";
	}
	
	@ModelAttribute("categorias")
	public List<String>  issae() {
	return Arrays.asList("Eletrônico", "Eletrodoméstico",
			"Alimento", "Prod. Limpeza", "Bebida", "Papelaria", "Higiene", "Outra");
	}
	
	
}
