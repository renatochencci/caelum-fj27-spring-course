package br.com.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.configuration.TokenManager;
import br.com.alura.forum.dto.output.AuthenticationTokenOutputDto;
import br.com.alura.forum.dto.output.LoginInputDto;

@RestController
@RequestMapping("api/auth")
public class UserAuthenticationController {
	
	@Autowired	
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenManager tokenManager;
	

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthenticationTokenOutputDto authenticate(@RequestBody LoginInputDto loginInfo) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				loginInfo.getEmail(), loginInfo.getPassword());
		Authentication authentication = authManager.authenticate(authToken);
		String token = tokenManager.GenerateToken(authentication);
		return new AuthenticationTokenOutputDto("Bearer",token);
	}
	
}
