package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r JOIN AccountRole ar ON r.id = ar.role.id JOIN Account a ON ar.account.id = a.id WHERE a.username = :username")
    List<Role> findRolesByAccountUsername(@Param("username") String username);

    Optional<Role> findByName(String name);
}
