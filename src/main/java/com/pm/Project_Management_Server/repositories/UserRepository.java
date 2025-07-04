package com.pm.Project_Management_Server.repositories;

import com.pm.Project_Management_Server.entity.User;
import com.pm.Project_Management_Server.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    List<User> findByUserType(UserType userType);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
