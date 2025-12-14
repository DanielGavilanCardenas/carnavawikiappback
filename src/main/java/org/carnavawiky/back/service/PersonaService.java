package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.PersonaRequest;
import org.carnavawiky.back.dto.PersonaResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.mapper.PersonaMapper;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.LocalidadRepository;
import org.carnavawiky.back.repository.PersonaRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaMapper personaMapper;

    // =======================================================
    // Helpers para relaciones
    // =======================================================

    private Localidad findLocalidad(Long id) {
        if (id == null) {
            throw new BadRequestException("El ID de la Localidad no puede ser nulo.");
        }
        return localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", id));
    }

    private Usuario findUsuario(Long id) {
        if (id == null) return null; // Es opcional
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    /**
     * Valida que el ID de Usuario no esté asociado a otra Persona.
     * @param usuarioId El ID de Usuario a comprobar.
     * @param personaIdActual El ID de la Persona que se está actualizando (o null si es una creación).
     */
    private void validarUsuarioUnico(Long usuarioId, Long personaIdActual) {
        if (usuarioId != null) {
            Optional<Persona> personaExistente = personaRepository.findByUsuario_Id(usuarioId);
            if (personaExistente.isPresent() && (personaIdActual == null || !personaExistente.get().getId().equals(personaIdActual))) {
                throw new BadRequestException("El Usuario con ID " + usuarioId + " ya está asociado a otra Persona.");
            }
        }
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public PersonaResponse crearPersona(PersonaRequest request) {

        // 1. Buscar y validar entidades relacionadas
        Localidad origen = findLocalidad(request.getLocalidadId());
        Usuario usuario = findUsuario(request.getUsuarioId());

        validarUsuarioUnico(request.getUsuarioId(), null);

        // 2. Mapear y guardar
        Persona persona = personaMapper.toEntity(request, origen, usuario);
        Persona nuevaPersona = personaRepository.save(persona);

        return personaMapper.toResponse(nuevaPersona);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public PersonaResponse obtenerPersonaPorId(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));
        return personaMapper.toResponse(persona);
    }

    // =======================================================
    // 3. OBTENER TODOS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // Búsqueda por nombre real o apodo
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<PersonaResponse> obtenerTodasPersonas(Pageable pageable, String search) {

        Page<Persona> personaPage;

        if (StringUtils.hasText(search)) {
            // Buscamos por nombre real o apodo
            personaPage = personaRepository.findByNombreRealContainingIgnoreCaseOrApodoContainingIgnoreCase(
                    search, search, pageable);
        } else {
            // Paginación normal
            personaPage = personaRepository.findAll(pageable);
        }

        Page<PersonaResponse> responsePage = personaPage.map(personaMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================
    @Transactional
    public PersonaResponse actualizarPersona(Long id, PersonaRequest request) {
        Persona personaExistente = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));

        // 1. Buscar y validar nuevas relaciones
        Localidad nuevoOrigen = findLocalidad(request.getLocalidadId());
        Usuario nuevoUsuario = findUsuario(request.getUsuarioId());

        // Validar unicidad del usuario para el PUT, excluyendo a la persona que estamos actualizando
        validarUsuarioUnico(request.getUsuarioId(), id);

        // 2. Actualizar campos
        personaExistente.setNombreReal(request.getNombreReal());
        personaExistente.setApodo(request.getApodo());
        personaExistente.setOrigen(nuevoOrigen);
        personaExistente.setUsuario(nuevoUsuario);

        // 3. Guardar y retornar
        Persona personaActualizada = personaRepository.save(personaExistente);

        return personaMapper.toResponse(personaActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================
    @Transactional
    public void eliminarPersona(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));

        // NOTA: La eliminación fallará si existen Componentes o Autores vinculados
        personaRepository.delete(persona);
    }
}