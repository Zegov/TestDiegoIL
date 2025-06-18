package com.sermaluc.prueba.apiuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sermaluc.prueba.apiuser.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, java.util.UUID> {
    User findByEmail(String email);
}
