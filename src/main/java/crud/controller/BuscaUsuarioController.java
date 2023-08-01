package crud.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import crud.model.Usuario;
import crud.repository.UsuarioRopository;

@Controller
public class BuscaUsuarioController {

	@Autowired
	private UsuarioRopository usuarioRopository;

    @GetMapping("/busca")
	public String cadastro(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		return "busca";
	}
	
	@GetMapping("/buscar")
	public String buscar(@RequestParam(name="descricao", required=false) String nome,
			             @RequestParam(name="mostrarTodosDados", required=false) Boolean mostrarTodosDados,
			             HttpSession sessao, ModelMap model) {
		
		List<Usuario> usuariosEncontrados = usuarioRopository.findByNome(nome);
		model.addAttribute("usuariosEncontrados", usuariosEncontrados);

		if(mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		
		return "busca";
	}
	
	@GetMapping("/estoque")
	public String estoque(ModelMap model, HttpSession sessao){
		List<Usuario> produtosCadastrados = (List<Usuario>) sessao.getAttribute("produtosCadastrados");
		List<Usuario> produtosEncontrados = new ArrayList<>();
		
		produtosEncontrados = produtosCadastrados;
		
		model.addAttribute("produtosEncontrados", produtosEncontrados);
		return "estoque";
	}
	
	@GetMapping("/remover/{id}")
	public String remover(@PathVariable("id") Integer idUsuario, RedirectAttributes attr, HttpSession sessao) {
		Usuario userLogado = (Usuario) sessao.getAttribute("userLogado");
		
		usuarioRopository.deleteById(idUsuario);
		
		if(idUsuario == userLogado.getId()) {
			sessao.setAttribute("userLogado", null);
		}
		
		attr.addAttribute("msgSucesso", "Remoção bem sucedida");
		
		return "redirect:/";
	}
	
	@GetMapping("/gerenciarUsuarios")
	public String gerenciarUsuarios(ModelMap model, HttpSession sessao) {
		
		List<Usuario> usuariosCadastrados = usuarioRopository.findAll();
		
		model.addAttribute("usuariosCadastrados" ,usuariosCadastrados);
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		return "todosUsuarios";
	}
}
