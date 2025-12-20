package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Méto necesario para buscar un Role por su nombre (Enum)
    Optional<Role> findByName(Role.RoleName name);
}