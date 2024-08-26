package com.example.onlinequizapp.controller;

import com.example.onlinequizapp.model.Score;
import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.service.ScoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ScoreController {

    @Autowired
    private ScoreService scoreService; // Assuming you have a ScoreService to handle score-related operations

//    @GetMapping("/showScore/{userId}/{quizId}")
//    public String showScore(@PathVariable("userId") Long userId, @PathVariable("quizId") Integer quizId, Model model) {
//        // Retrieve the score for the user and quiz from the database
//        Score score = scoreService.getScoreByUserIdAndQuizId(userId, quizId);
//
//        // Add the score to the model to be displayed in the view
//        model.addAttribute("score", score);
//        model.addAttribute("quizId", quizId);
//        model.addAttribute("userId", userId);
//
//        // Return the name of the view template for showing the score (e.g., "show_score")
//        return "show_score";
//    }


}
