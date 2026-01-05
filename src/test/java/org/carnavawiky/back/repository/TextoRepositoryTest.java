package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Texto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TextoRepositoryTest {

    @Autowired
    private TextoRepository textoRepository;

    @Autowired
    private TestEntityManager entityManager;

    // =======================================================
    // 1. PRUEBA DE CREACIÓN Y BÚSQUEDA POR KEY
    // =======================================================
    @Test
    void testSaveAndFindByKey() {
        // Arrange
        Texto texto = new Texto();
        texto.setKey("welcome_msg");
        texto.setValue("Bienvenido");
        entityManager.persist(texto);
        entityManager.flush();

        // Act
        Optional<Texto> foundTexto = textoRepository.findByKey("welcome_msg");

        // Assert
        assertThat(foundTexto).isPresent();
        assertThat(foundTexto.get().getValue()).isEqualTo("Bienvenido");
    }

    @Test
    void testFindByKey_NotFound() {
        // Act
        Optional<Texto> foundTexto = textoRepository.findByKey("non_existent_key");

        // Assert
        assertThat(foundTexto).isNotPresent();
    }

    // =======================================================
    // 2. PRUEBA DE ELIMINACIÓN POR KEY
    // =======================================================
    @Test
    void testDeleteByKey() {
        // Arrange
        Texto texto = new Texto();
        texto.setKey("to_delete");
        texto.setValue("Delete me");
        entityManager.persist(texto);
        entityManager.flush();

        // Act
        textoRepository.deleteByKey("to_delete");
        
        // Es necesario hacer flush/clear o buscar de nuevo para verificar que se borró en la DB
        // deleteByKey es una query derivada, Spring Data JPA la ejecuta.
        // Verificamos si sigue existiendo.
        Optional<Texto> foundTexto = textoRepository.findByKey("to_delete");

        // Assert
        assertThat(foundTexto).isNotPresent();
    }

    // =======================================================
    // 3. PRUEBA DE ACTUALIZACIÓN
    // =======================================================
    @Test
    void testUpdate() {
        // Arrange
        Texto texto = new Texto();
        texto.setKey("update_key");
        texto.setValue("Old Value");
        entityManager.persist(texto);
        entityManager.flush();

        // Act
        Texto found = textoRepository.findByKey("update_key").get();
        found.setValue("New Value");
        textoRepository.save(found);

        // Assert
        Texto updated = textoRepository.findByKey("update_key").get();
        assertThat(updated.getValue()).isEqualTo("New Value");
    }
}