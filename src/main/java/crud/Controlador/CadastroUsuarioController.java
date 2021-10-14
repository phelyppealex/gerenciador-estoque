package crud.Controlador;

import crud.Produtos.Produto;
import crud.Usuarios.Usuario;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class CadastroUsuarioController {

    @PostMapping("/salvar")
	public String salvar(Usuario usuario, RedirectAttributes attr, HttpSession sessao){
		
		Integer id = (Integer) sessao.getAttribute("idUsuario");
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("usuariosCadastrados");
		attr.addAttribute("pessoa", (Usuario)sessao.getAttribute("userLogado"));
		
		List<String> msgErro = validarDados(usuario);

		if(!msgErro.isEmpty()){
			attr.addFlashAttribute("msgErro", msgErro);
		}

		if(id == null) {
			id = 1;
		}
		
		if(usuariosCadastrados == null) {
			usuariosCadastrados = new ArrayList<>();
		}
		
		//Testando se é edição ou cadastro
		if(usuario.getId() == 0) {
			//Cadastro
			usuario.setId(id);
			usuariosCadastrados.add(usuario);
			
			id++;
			sessao.setAttribute("idUsuario", id);
			sessao.setAttribute("usuariosCadastrados", usuariosCadastrados);
			
			attr.addFlashAttribute("msgSucesso", "Cadastrado com sucesso");
		}else {
			//Edição
			usuariosCadastrados.remove(usuario);
			usuariosCadastrados.add(usuario);
			attr.addFlashAttribute("msgSucesso", "Edição bem sucedida!");
		}
		
		return "redirect:/";
	}

	@GetMapping("/editarUsuario/{id}")
	public String editarProduto(@PathVariable("id") Integer idUsuario, HttpSession sessao, ModelMap model) {
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("usuariosCadastrados");
		
		Usuario u = new Usuario();
		u.setId(idUsuario);
		
		int posicao = usuariosCadastrados.indexOf(u);
		u = usuariosCadastrados.get(posicao);
		
		model.addAttribute("usuario", u);
		
		return "editar";
	}
	
	@PostMapping("/login")
	public String login(Usuario login, HttpSession sessao, ModelMap model) {
		
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("usuariosCadastrados");
		Usuario userLogado = (Usuario) sessao.getAttribute("userLogado");
		
		boolean achouUsuario = false;
		
		if(usuariosCadastrados == null) {
			model.addAttribute("msgErro", "- Não existem usuários ainda, cadastre-os");
			return "signIn";
		}
		
		for (Usuario log : usuariosCadastrados) {
			if( log.getEmail() .equals(login.getEmail()) && log.getSenha() .equals(login.getSenha()) ) {
				CadastroProdutoController categ = new CadastroProdutoController();
				sessao.setAttribute("userLogado", log);
				model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
				model.addAttribute("msgSucesso", "Login efetuado com sucesso!");
				model.addAttribute("categorias", categ.issae());
				achouUsuario = true;
			}
		}
		
		if(!achouUsuario) {
			model.addAttribute("msgErro", "Email ou senha incorretos");
			return "signIn";
		}
		
		model.addAttribute("produto", new Produto());
		
		return "cadastro";
	}
	
	@GetMapping("/logoff")
	public String logoff(HttpSession sessao, ModelMap model) {
		
		sessao.setAttribute("userLogado", null);
		model.addAttribute("pessoa", null);
		
		return "inicio";
	}
	
	public List<String> validarDados(Usuario usuario){

		List<String> msgs = new ArrayList<>();

		if(usuario.getNome() == null || usuario.getNome().isEmpty()){
			msgs.add("- O campo ''Nome'' não foi preenchido!");
		}
		if(usuario.getEmail() == null || usuario.getEmail().isEmpty()){
			msgs.add("- O campo ''Email'' não foi preenchido!");
		}
		if(usuario.getSenha() == null || usuario.getSenha().isEmpty()){
			msgs.add("- O campo ''Senha'' não foi preenchido!");
		}

		return msgs;
	}
	
	public List<String> validarDadosLogin(Usuario usuario, HttpSession sessao){
		
		List<String> msgs = new ArrayList<>();
		Usuario userLogado = (Usuario) sessao.getAttribute("userLogado");
		
		if(usuario.getEmail() == null || usuario.getEmail().isEmpty()){
			msgs.add("- O campo ''Email'' não foi preenchido!");
		}
		if(usuario.getSenha() == null || usuario.getSenha().isEmpty()){
			msgs.add("- O campo ''Senha'' não foi preenchido!");
		}
		
		return null;
	}


	@GetMapping("/paginadelogin")
	public String paginaDeLogin(ModelMap model, HttpSession sessao){
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		model.addAttribute("usuario", new Usuario());
		return "signIn";
	}
	
	@GetMapping("/paginadecadastro")
	public String paginaDeCadastro(ModelMap model, HttpSession sessao) {
		model.addAttribute("usuario", new Usuario());
		return "signUp";
	}
	
	@GetMapping("/update")
	public String update(HttpSession sessao, ModelMap model){
		Usuario userLogado = (Usuario) sessao.getAttribute("userLogado");
		model.addAttribute("usuario", userLogado);
		return "editarUser";
	}
	
	@PostMapping("/editar")
	public String editar(Usuario usuario, HttpSession sessao, ModelMap model) {
		sessao.setAttribute("userLogado", usuario);
		List<Usuario> usuariosCadastrados = (List<Usuario>) sessao.getAttribute("usuariosCadastrados");
		
		Usuario u = new Usuario();
		u.setId(usuario.getId());

		usuariosCadastrados.remove(u);
		usuariosCadastrados.add(usuario);
		
		sessao.setAttribute("userLogado", usuario);
		sessao.setAttribute("usuariosCadastrados", usuariosCadastrados);
		
		model.addAttribute("pessoa", usuario);
		
		return "inicio";
	}
}