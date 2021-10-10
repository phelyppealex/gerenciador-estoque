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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import crud.Usuarios.Usuario;

@Controller
public class BuscaUsuarioController {
    @GetMapping("/busca")
	public String cadastro(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		return "busca";
	}
	
	@GetMapping("/buscar")
	public String buscar(@RequestParam(name="descricao", required=false) String nome,
			             @RequestParam(name="mostrarTodosDados", required=false) Boolean mostrarTodosDados,
			             HttpSession sessao, ModelMap model) {
		
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("produtosCadastrados");
		List<Usuario> usuariosEncontrados = new ArrayList<>();
		
		if((nome == null || nome.isEmpty()) & usuariosCadastrados != null) {
			
			usuariosEncontrados = usuariosCadastrados;
			
		}else if((nome != null || !nome.isEmpty()) & usuariosCadastrados != null){
			
			usuariosEncontrados = (List<Usuario>) usuariosCadastrados.stream()
					.filter(
						u -> u.getNome().toLowerCase().contains(
							nome.toLowerCase()
						)
					).collect(Collectors.toList());
		}
		
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
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("usuariosCadastrados");
		Usuario userLogado = (Usuario) sessao.getAttribute("userLogado");
		
		Usuario p = new Usuario();
		p.setId(idUsuario);
		
		boolean removeu = usuariosCadastrados.remove(p);
		
		if(p.getId() == userLogado.getId()) {
			sessao.setAttribute("userLogado", null);
		}
		
		if(removeu) {
			attr.addAttribute("msgSucesso", "Remoção bem sucedida");
		}else {
			attr.addAttribute("msgErro", "O produto não foi removido");
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/gerenciarUsuarios")
	public String gerenciarUsuarios(ModelMap model, HttpSession sessao) {
		
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("usuariosCadastrados");
		
		model.addAttribute("usuariosCadastrados" ,usuariosCadastrados);
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		return "todosUsuarios";
	}
}
