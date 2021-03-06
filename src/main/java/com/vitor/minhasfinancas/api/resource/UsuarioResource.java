package com.vitor.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitor.minhasfinancas.api.dto.UsuarioDTO;
import com.vitor.minhasfinancas.exception.ErrorAuthentication;
import com.vitor.minhasfinancas.exception.RegraNegocioException;
import com.vitor.minhasfinancas.model.entity.Usuario;
import com.vitor.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	@PostMapping("/authenticate")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario authenticated = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(authenticated);
		}catch (ErrorAuthentication e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
		
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
