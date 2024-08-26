package com.example.onlinequizapp.service;

import com.example.onlinequizapp.model.Score;
import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }

    public Score getScoreByUserIdAndQuizId(Long userId, Integer quizId) {
        return scoreRepository.findByUserIdAndQuizId(userId, quizId);
    }

    public List<Score> getScoreByUser(Integer userId) {
        return scoreRepository.findByUserId(userId);
    }
}