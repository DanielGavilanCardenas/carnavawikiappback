package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.UsuarioRequest;
import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.UsuarioMapper;
import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository; // Necesario para gestionar los roles

    @Autowired
    private PasswordEncoder passwordEncoder; // Necesario para cifrar contraseñas

    @Autowired
    private UsuarioMapper usuarioMapper;




    // =======================================================
    // 1. CREAR (POST)
    // =======================================================

    @Transactional
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        // Validación de unicidad de username y email
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El nombre de usuario ya está en uso.");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está en uso.");
        }

        // 1. Cargar Roles
        Set<Role> roles = request.getRoleIds().stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id)))
                .collect(Collectors.toSet());

        // 2. Crear y configurar Usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword())); // Cifrar la contraseña
        usuario.setRoles(roles);
        usuario.setEnabled(request.getEnabled());
        // activationToken se dejará nulo, ya que el admin lo activa directamente

        // 3. Guardar y retornar
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(nuevoUsuario);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        return usuarioMapper.toResponse(usuario);
    }

    // =======================================================
    // 3. OBTENER TODOS (GET)
    // =======================================================

    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================


    @Transactional
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // 1. Validar unicidad de email (solo si cambia)
        if (!usuarioExistente.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está en uso por otro usuario.");
        }

        // 2. Actualizar campos

        usuarioExistente.setEmail(request.getEmail());

        // Si se proporciona una nueva contraseña, cifrarla y actualizarla
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        usuarioExistente.setEnabled(request.getEnabled());

        // 3. Actualizar Roles (Lógica que debe estar correcta)
        Set<Role> nuevosRoles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId)))
                .collect(Collectors.toSet());
        usuarioExistente.setRoles(nuevosRoles);

        // 4. Guardar y retornar
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponse(usuarioActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================

    @Transactional
    public void eliminarUsuario(Long id) {
        // Se puede añadir lógica aquí para evitar que un admin se elimine a sí mismo
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        usuarioRepository.deleteById(id);
    }
}