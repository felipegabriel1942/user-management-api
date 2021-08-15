package com.felipegabriel.usermanagementapi.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipegabriel.usermanagementapi.api.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

}
