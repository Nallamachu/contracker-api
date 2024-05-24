package com.msrts.contracker.repository;

import com.msrts.contracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query(value = "from User u where u.email=:email")
  User findByEmail(String email);

}
