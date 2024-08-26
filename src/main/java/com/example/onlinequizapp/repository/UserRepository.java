package com.example.onlinequizapp.repository;

import com.example.onlinequizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public Long countById(Integer id);

    User findByEmailAndPassword(String email, String password);


}



