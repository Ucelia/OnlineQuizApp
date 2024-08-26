package com.example.onlinequizapp.repository;

import com.example.onlinequizapp.model.Score;
import com.example.onlinequizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Score findByUserIdAndQuizId(Long userId, Integer quizId);

    List<Score> findByUserId(Integer userId);
}