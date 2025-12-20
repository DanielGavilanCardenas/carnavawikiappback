package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("carnavalero");
        usuario.setEmail("test@carnaval.com");
        usuario.setPassword("password123");
        usuario.setActivationToken("token-activacion");
        usuario.setResetToken("token-reseteo");
        entityManager.persist(usuario);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar usuario por username")
    void testFindByUsername() {
        Optional<Usuario> found = usuarioRepository.findByUsername("carnavalero");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@carnaval.com");
    }

    @Test
    @DisplayName("Debe verificar si existe por username o email")
    void testExistsMethods() {
        assertThat(usuarioRepository.existsByUsername("carnavalero")).isTrue();
        assertThat(usuarioRepository.existsByEmail("test@carnaval.com")).isTrue();
        assertThat(usuarioRepository.existsByUsername("inexistente")).isFalse();
    }

    @Test
    @DisplayName("Debe encontrar por token de activación")
    void testFindByActivationToken() {
        Optional<Usuario> found = usuarioRepository.findByActivationToken("token-activacion");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("carnavalero");
    }

    @Test
    @DisplayName("Debe encontrar por token de reseteo")
    void testFindByResetToken() {
        Optional<Usuario> found = usuarioRepository.findByResetToken("token-reseteo");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("carnavalero");
    }

    @Test
    @DisplayName("Debe buscar por username o email ignorando mayúsculas con paginación")
    void testSearchByUsernameOrEmail() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act: Buscamos por parte del nombre en mayúsculas
        Page<Usuario> result = usuarioRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                "CARNAVAL", "CARNAVAL", pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo("carnavalero");
    }
}