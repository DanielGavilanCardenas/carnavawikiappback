package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.UsuarioRequest;
import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.UsuarioMapper;
import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public PageResponse<UsuarioResponse> obtenerTodosUsuarios(Pageable pageable, String search) {
        Page<Usuario> usuariosPage;
        if (StringUtils.hasText(search)) {
            usuariosPage = usuarioRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            usuariosPage = usuarioRepository.findAll(pageable);
        }

        List<UsuarioResponse> contenido = usuariosPage.getContent().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<UsuarioResponse>builder()
                .content(contenido)
                .pageNumber(usuariosPage.getNumber())
                .pageSize(usuariosPage.getSize())
                .totalElements(usuariosPage.getTotalElements())
                .totalPages(usuariosPage.getTotalPages())
                .build();
    }

    @Transactional
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        if (request.getEmail() != null && !usuario.getEmail().equalsIgnoreCase(request.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("El email ya está en uso.");
            }
            usuario.setEmail(request.getEmail());
        }

        if (StringUtils.hasText(request.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getEnabled() != null) {
            usuario.setEnabled(request.getEnabled());
        }

        // CORRECCIÓN AQUÍ: Añadimos .filter(Objects::nonNull)
        if (request.getRoleIds() != null) {
            Set<Role> nuevosRoles = request.getRoleIds().stream()
                    .filter(java.util.Objects::nonNull) // <--- Evita que IDs nulos rompan la búsqueda
                    .map(rId -> roleRepository.findById(rId)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", rId)))
                    .collect(Collectors.toSet());
            usuario.setRoles(nuevosRoles);
        }

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Transactional
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El nombre de usuario ya existe.");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);

        if (request.getRoleIds() != null) {
            Set<Role> roles = request.getRoleIds().stream()
                    .map(rId -> roleRepository.findById(rId)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", rId)))
                    .collect(Collectors.toSet());
            usuario.setRoles(roles);
        }
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
}