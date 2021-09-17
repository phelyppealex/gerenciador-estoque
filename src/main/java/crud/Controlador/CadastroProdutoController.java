package crud.Controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

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

@Controller
@RequestMapping("/produto")
public class CadastroProdutoController {

	@GetMapping("/cadastro")
	public String cadastro(ModelMap model) {
		model.addAttribute("produto", new Produto());
		model.addAttribute("usuario", new Usuario());
		return "cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Produto produto, RedirectAttributes attr, HttpSession sessao){
		
		Integer id = (Integer) sessao.getAttribute("id");
		List<Produto> produtosCadastrados = (List<Produto>) sessao.getAttribute("produtosCadastrados");

		List<String> msgErro = validarDados(produto);

		if(!msgErro.isEmpty()){
			attr.addFlashAttribute("msgErro", msgErro);
		}

		if(id == null) {
			id = 1;
		}
		
		if(produtosCadastrados == null) {
			produtosCadastrados = new ArrayList<>();
		}
		
		//Testando se é edição ou cadastro
		if(produto.getId() == 0) {
			//Cadastro
			produto.setId(id);
			produtosCadastrados.add(produto);
			
			id++;
			sessao.setAttribute("id", id);
			sessao.setAttribute("produtosCadastrados", produtosCadastrados);
			
			attr.addFlashAttribute("msgSucesso", "Cadastrado com sucesso");
		}else {
			//Edição
			produtosCadastrados.remove(produto);
			produtosCadastrados.add(produto);
			attr.addFlashAttribute("msgSucesso", "Edição bem sucedida!");
		}
		
		return "redirect:/produto/cadastro";
	}
	
	@GetMapping("/editar/{id}")
	public String editarProduto(@PathVariable("id") Integer idProduto, HttpSession sessao, ModelMap model) {
		List<Produto> produtosCadastrados = (List<Produto>) sessao.getAttribute("produtosCadastrados");
		
		Produto p = new Produto();
		p.setId(idProduto);
		
		int posicao = produtosCadastrados.indexOf(p);
		p = produtosCadastrados.get(posicao);
		
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
	public String mensagem(ModelMap model) {
		int id = 0;
		
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
