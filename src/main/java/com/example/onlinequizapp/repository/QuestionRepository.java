package com.example.onlinequizapp.repository;

import com.example.onlinequizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuizId (Integer quizId);

}
