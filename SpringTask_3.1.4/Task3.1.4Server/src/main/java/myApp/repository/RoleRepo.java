package myApp.repository;

import myApp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    void deleteRolesById(Long id);
}
