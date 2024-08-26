package com.example.onlinequizapp.service;

import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.repository.UserRepository;
import com.example.onlinequizapp.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;
    public List<User> listAll(){
        return (List<User>) repo.findAll();
    }


//    public void registerUser(User user) {
//        repo.save(user);
//    }
//
//    public User user(String username, String password){
//        User user = repo.findByUsernameAndPassword(username,password);
//        return user;
//    }

    public void save(User user) {
        repo.save(user);
    }

    public User get(Integer id) throws UserNotFoundException {
        Optional<User> result = repo.findById(id);
        if (result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("Could not find any users with ID " + id);
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new UserNotFoundException("Could not find any users with ID " + id);
        }
        repo.deleteById(id);
    }

    public User authenticateByEmail(String email, String password) {
        return repo.findByEmailAndPassword(email, password);
    }

}


