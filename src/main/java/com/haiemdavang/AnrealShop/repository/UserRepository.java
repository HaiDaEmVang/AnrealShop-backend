package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);
}
