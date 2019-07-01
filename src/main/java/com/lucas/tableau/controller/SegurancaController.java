package com.lucas.tableau.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SegurancaController {
	
	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user){
		
		//se o usuario  estiver logado ele e redirecionado para a pagina de pesquisa
		if(user != null){
			return "redirect:/golden";
		}
		
		return "login";
	}
	

}
