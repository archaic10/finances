package com.vitor.minhasfinancas.service.impl;


import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitor.minhasfinancas.exception.ErrorAuthentication;
import com.vitor.minhasfinancas.exception.RegraNegocioException;
import com.vitor.minhasfinancas.model.entity.Usuario;
import com.vitor.minhasfinancas.model.repository.UsuarioRepository;
import com.vitor.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
		
	private UsuarioRepository repository;
		
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		Optional<Usuario> user = repository.findByEmail(email);
		
		if(!user.isPresent()){
			throw new ErrorAuthentication("Not exist user registred for email informated!");
		}
		
		if(!user.get().getSenha().equals(senha)) {
			throw new ErrorAuthentication("Password Invalidated");
		}
		return user.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}
	
}
