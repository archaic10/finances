package com.vitor.minhasfinancas.service;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.vitor.minhasfinancas.exception.ErrorAuthentication;
import com.vitor.minhasfinancas.exception.RegraNegocioException;
import com.vitor.minhasfinancas.model.entity.Usuario;
import com.vitor.minhasfinancas.model.repository.UsuarioRepository;
import com.vitor.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test(expected = Test.None.class)
	public void saveUser() {
		// cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@email.com").senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		// acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

		// verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");

	}

	@Test(expected = RegraNegocioException.class)
	public void errorInSaveUserWithEmailRegistred() {
		// Cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

		// acao
		service.salvarUsuario(usuario);

		// verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}

	@Test(expected = Test.None.class)
	public void validateEmail() {

		// Cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// acao
		service.validarEmail("usuario@email.com");

	}

	@Test(expected = Test.None.class)
	public void auth() {
		// cenário
		String email = "email@email.com";
		String senha = "aa";

		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

		// acao
		Usuario result = service.autenticar(email, senha);

		// verificacao
		Assertions.assertThat(result).isNotNull();
	}

	@Test(expected = ErrorAuthentication.class)
	public void errorAuth() {
		// cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// acao
		service.autenticar("email@email.com", "senha");
	}

	@Test
	public void errorAuthPassword() {
		// cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErrorAuthentication.class).hasMessage("Password Invalidated");
	}

	@Test
	public void errorFindingRegistredUser() {
		// cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErrorAuthentication.class)
				.hasMessage("Not exist user registred for email informated!");
	}

	@Test(expected = RegraNegocioException.class)
	public void validateEmailRegistred() {
		// Cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		// acao
		service.validarEmail("tomcat@email.com");
	}
}
