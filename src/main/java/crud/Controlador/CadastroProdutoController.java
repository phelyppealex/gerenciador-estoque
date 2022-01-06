package crud.Controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import crud.Produtos.Produto;
import crud.Usuarios.Usuario;
import crud.repository.ProdutoRepository;
import crud.repository.UsuarioRopository;

@Controller
@RequestMapping("/produto")
public class CadastroProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping("/cadastro")
	public String cadastro(ModelMap model, HttpSession sessao) {
		model.addAttribute("produto", new Produto());
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("categorias", issae());
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		return "cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Produto produto, RedirectAttributes attr, HttpSession sessao){
		
		List<String> msgErro = validarDados(produto);

		if(!msgErro.isEmpty()){
			attr.addFlashAttribute("msgErro", msgErro);
		}
	
		produtoRepository.save(produto);
		attr.addFlashAttribute("msgSucesso", "Edição bem sucedida!");
		attr.addFlashAttribute("pessoa", sessao.getAttribute("userLogado"));
		
		return "redirect:/produto/cadastro";
	}
	
	@GetMapping("/editar/{id}")
	public String editarProduto(@PathVariable("id") Integer idProduto, HttpSession sessao, ModelMap model) {
		
		Produto p = produtoRepository.findById(idProduto).get();
		
		model.addAttribute("produto", p);
		
		return "editar";
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
	
	@GetMapping("/mensagem")
	public String mensagem(ModelMap model, HttpSession sessao) {
		int id = 0;
		
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("id", id);
		model.addAttribute("mensagem", "Não é possível acessar esta página por aqui, é preciso que vá para a página de buscas ou estoque incialmente. Ex.:");
		
		return "editar";
	}
	
	@ModelAttribute("categorias")
	public List<String>  issae() {
	return Arrays.asList("Eletrônico", "Eletrodoméstico",
			"Alimento", "Prod. Limpeza", "Bebida", "Papelaria", "Higiene", "Outra");
	}
	
}
