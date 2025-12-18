package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Persona;
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
class PersonaRepositoryTest {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Localidad localidadCadiz;

    @BeforeEach
    void setUp() {
        // 1. Crear y persistir Localidad (Requisito obligatorio para Persona)
        localidadCadiz = new Localidad();
        localidadCadiz.setNombre("Cádiz");
        localidadCadiz = localidadRepository.save(localidadCadiz);

        // 2. Crear personas de prueba
        Persona p1 = new Persona();
        p1.setNombreReal("Juan Pérez");
        p1.setApodo("El Juani");
        p1.setOrigen(localidadCadiz);

        Persona p2 = new Persona();
        p2.setNombreReal("Maria Garcia");
        p2.setApodo("La Mari");
        p2.setOrigen(localidadCadiz);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar una persona por su nombre real exacto")
    void testFindByNombreReal() {
        // ACT
        Optional<Persona> encontrada = personaRepository.findByNombreReal("Juan Pérez");

        // ASSERT
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getApodo()).isEqualTo("El Juani");
    }

    @Test
    @DisplayName("Debe buscar personas por coincidencia parcial (Search) ignorando mayúsculas")
    void testSearchByNombreOrApodo() {
        Pageable pageable = PageRequest.of(0, 10);

        // Buscamos "mari" (en minúsculas) para probar el IgnoreCase
        Page<Persona> resultado = personaRepository.findByNombreRealContainingIgnoreCaseOrApodoContainingIgnoreCase(
                "mari", "mari", pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getNombreReal()).isEqualTo("Maria Garcia");
    }

    @Test
    @DisplayName("Debe guardar una nueva persona con su relación a localidad")
    void testSavePersona() {
        Persona nueva = new Persona();
        nueva.setNombreReal("Antonio Martín");
        nueva.setApodo("El Niño");
        nueva.setOrigen(localidadCadiz);

        Persona guardada = personaRepository.save(nueva);

        assertThat(guardada.getId()).isNotNull();
        Persona buscada = entityManager.find(Persona.class, guardada.getId());
        assertThat(buscada.getNombreReal()).isEqualTo("Antonio Martín");
    }

    @Test
    @DisplayName("Debe eliminar una persona correctamente")
    void testDeletePersona() {
        Persona p = personaRepository.findByNombreReal("Juan Pérez").orElseThrow();

        personaRepository.delete(p);
        entityManager.flush();

        Optional<Persona> eliminada = personaRepository.findByNombreReal("Juan Pérez");
        assertThat(eliminada).isEmpty();
    }
}