package crud.controller;

import crud.model.Produto;
import crud.service.ProdutoService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

	private ProdutoService service;

	public ProdutoController(ProdutoService service){
		this.service = service;
	}

	@GetMapping("/cadastro")
	public String cadastro(ModelMap model, HttpSession sessao) {
		model.addAttribute("produto", new Produto());
		model.addAttribute("categorias", getCategorias());
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		return "cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Produto produto, RedirectAttributes attr, HttpSession sessao){
		List<String> msgErro = validarDados(produto);
		attr.addFlashAttribute("pessoa", sessao.getAttribute("userLogado"));

		if(!msgErro.isEmpty()){
			attr.addFlashAttribute("msgErro", msgErro);
			return "redirect:/produto/cadastro";
		}
	
		service.save(produto);
		attr.addFlashAttribute("msgSucesso", "Edição bem sucedida!");
		
		return "redirect:/produto/cadastro";
	}
	
	@GetMapping("/busca")
	public String busca(){
		return "busca";
	}
	
	@GetMapping("/buscar")
	public String buscar(
		@RequestParam(name="descricao", required=false) String descricao,
		@RequestParam(name="mostrarTodosDados", required=false) Boolean mostrarTodosDados,
		HttpSession sessao, ModelMap model
	){
		
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("produtosEncontrados", service.findByDescricao(descricao));
		
		if(!mostrarTodosDados) {
			model.addAttribute("mostrarTodosDados", true);
		}
		return "busca";
	}
	
	@GetMapping("/estoque")
	public String estoque(ModelMap model, HttpSession sessao){

		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("produtosEncontrados", service.findAll());

		return "estoque";
	}
	
	@GetMapping("/editar/{id}")
	public String editarProduto(@PathVariable("id") Integer idProduto, HttpSession sessao, ModelMap model) {
		
		Produto p = service.findById(idProduto);
		
		model.addAttribute("produto", p);
		
		return "editar";
	}

	@GetMapping("/remover/{id}")
	public String remover(@PathVariable("id") Integer idProduto, RedirectAttributes attr, HttpSession sessao) {
		
		service.delete(idProduto);
		attr.addAttribute("msgSucesso", "Remoção bem sucedida");
		
		return "redirect:/produto/buscar";
	}

	@GetMapping("/mensagem")
	public String mensagem(ModelMap model, HttpSession sessao) {
		int id = 0;
		
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("id", id);
		model.addAttribute("mensagem", "Não é possível acessar esta página por aqui, é preciso que vá para a página de buscas ou estoque incialmente. Ex.:");
		
		return "editar";
	}
	
	@ModelAttribute("categorias")
	public List<String> getCategorias() {
		return Arrays.asList(
			"Eletrônico",
			"Eletrodoméstico",
			"Alimento",
			"Prod. Limpeza",
			"Bebida",
			"Papelaria",
			"Higiene",
			"Outra"
		);
	}

	public List<String> validarDados(Produto produto){

		List<String> msgs = new ArrayList<>();

		if(produto.getDescricao() == null || produto.getDescricao().isEmpty()){
			msgs.add("- O campo ''Descrição'' não foi preenchido!");
		}
		if(produto.getCategoria() == null || produto.getDescricao().isEmpty()){
			msgs.add("- Selecione uma categoria!");
		}

		return msgs;
	}
}