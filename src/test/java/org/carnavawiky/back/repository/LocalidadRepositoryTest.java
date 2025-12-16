package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Localidad; // ASUMIDO: Ajuste el paquete de la entidad si es diferente
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para LocalidadRepository.
 */
@DataJpaTest
public class LocalidadRepositoryTest {

    @Autowired
    private LocalidadRepository localidadRepository;

    // Utilidad de Spring para configurar y limpiar la DB de prueba
    @Autowired
    private TestEntityManager entityManager;

    // =======================================================
    // 1. PRUEBA DE CREACIÓN Y BÚSQUEDA POR ID
    // =======================================================
    @Test
    void testSaveAndFindById() {
        // Arrange
        Localidad newLocalidad = new Localidad();
        newLocalidad.setNombre("San Fernando");

        // Act
        Localidad savedLocalidad = localidadRepository.save(newLocalidad);
        Optional<Localidad> foundLocalidad = localidadRepository.findById(savedLocalidad.getId());

        // Assert
        assertThat(foundLocalidad).isPresent();
        assertThat(foundLocalidad.get().getNombre()).isEqualTo("San Fernando");
    }

    // =======================================================
    // 2. PRUEBA DE BÚSQUEDA DE TODOS
    // =======================================================
    @Test
    void testFindAll() {
        // Arrange
        Localidad cadi = new Localidad();
        cadi.setNombre("Cádiz");
        entityManager.persist(cadi);

        Localidad jerez = new Localidad();
        jerez.setNombre("Jerez");
        entityManager.persist(jerez);

        entityManager.flush();

        // Act
        List<Localidad> localidades = localidadRepository.findAll();

        // Assert
        assertThat(localidades).hasSize(2);
        assertThat(localidades).extracting(Localidad::getNombre).containsExactlyInAnyOrder("Cádiz", "Jerez");
    }

    // =======================================================
    // 3. PRUEBA DE ACTUALIZACIÓN
    // =======================================================
    @Test
    void testUpdate() {
        // Arrange
        Localidad existingLocalidad = new Localidad();
        existingLocalidad.setNombre("El Puerto de Santa María");
        Localidad savedLocalidad = entityManager.persistFlushFind(existingLocalidad);

        // Act
        savedLocalidad.setNombre("El Puerto");
        Localidad updatedLocalidad = localidadRepository.save(savedLocalidad);

        // Assert
        assertThat(updatedLocalidad.getNombre()).isEqualTo("El Puerto");
    }

    // =======================================================
    // 4. PRUEBA DE ELIMINACIÓN
    // =======================================================
    @Test
    void testDelete() {
        // Arrange
        Localidad localidadToDelete = new Localidad();
        localidadToDelete.setNombre("Rota");
        Localidad savedLocalidad = entityManager.persistFlushFind(localidadToDelete);

        // Act
        localidadRepository.deleteById(savedLocalidad.getId());

        // Assert
        Optional<Localidad> foundLocalidad = localidadRepository.findById(savedLocalidad.getId());
        assertThat(foundLocalidad).isNotPresent();
    }

    // =======================================================
    // 5. PRUEBAS DE BÚSQUEDA POR NOMBRE (Usando el método existente)
    // =======================================================

    @Test
    void testFindByNombreContainingIgnoreCase_ExistingAndFullMatch() {
        // Arrange: Creamos un Pageable (página 0, tamaño 10)
        Pageable pageable = PageRequest.of(0, 10);
        Localidad chiclana = new Localidad();
        chiclana.setNombre("Chiclana");
        entityManager.persist(chiclana);
        entityManager.flush();

        // Act: Búsqueda exacta del nombre
        Page<Localidad> page = localidadRepository.findByNombreContainingIgnoreCase("Chiclana", pageable);

        // Assert: Debe encontrar 1 resultado con el nombre correcto
        assertThat(page).hasSize(1);
        assertThat(page.getContent().get(0).getNombre()).isEqualTo("Chiclana");
    }

    @Test
    void testFindByNombreContainingIgnoreCase_PartialMatch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Localidad sanRoque = new Localidad();
        sanRoque.setNombre("San Roque");
        entityManager.persist(sanRoque);

        Localidad sanFernando = new Localidad();
        sanFernando.setNombre("San Fernando");
        entityManager.persist(sanFernando);
        entityManager.flush();

        // Act: Buscar "San"
        Page<Localidad> page = localidadRepository.findByNombreContainingIgnoreCase("San", pageable);

        // Assert: Debe encontrar 2 resultados
        assertThat(page).hasSize(2);
        assertThat(page.getContent()).extracting(Localidad::getNombre).containsExactlyInAnyOrder("San Roque", "San Fernando");
    }

    @Test
    void testFindByNombreContainingIgnoreCase_NotFound() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Localidad> page = localidadRepository.findByNombreContainingIgnoreCase("Tarifa", pageable);

        // Assert
        assertThat(page).isEmpty();
    }
}