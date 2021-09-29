package crud.Controlador;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

	@GetMapping("/")
	public String entrar(ModelMap model, HttpSession sessao) {
		model.addAttribute("pessoa", sessao.getAttribute("userLogado"));
		return "inicio";
	}
}