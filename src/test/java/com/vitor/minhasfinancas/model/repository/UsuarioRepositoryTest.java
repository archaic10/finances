package com.vitor.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vitor.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void VerificarExistenciaEmail() {
		// Cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		entityManager.persist(usuario);

		// ação/ Execução
		boolean result = repository.existsByEmail("usuario@email.com");

		// verificação
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void validaUsuarioCadastradoEmail() {
		// Execução
		boolean result = repository.existsByEmail("usuario@email.com");

		// Validação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void createUser() {
		//cenario
		Usuario user = createUserGeneric();
		
		// execucao
		Usuario response = repository.save(user);
		
		// verificacao
		Assertions.assertThat(response.getId()).isNotNull();
	}
	
	@Test
	public void findUserbyEmail() {
		Usuario user = createUserGeneric();
		// execucao
		entityManager.persist(user);
		
		//verificacao		
		Optional<Usuario> response = repository.findByEmail("testazildo@email.com");
		Assertions.assertThat(response.isPresent()).isTrue();
	}
	
	public static Usuario createUserGeneric() {
		return Usuario.builder().nome("Testazildo").email("testazildo@email.com").senha("123").build();
	}
}
