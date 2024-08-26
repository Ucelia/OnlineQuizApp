package com.example.onlinequizapp.controller;


import com.example.onlinequizapp.model.Quiz;
import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.service.QuizService;
import com.example.onlinequizapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public UserService userService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/createquiz")
    public String showQuizCreateForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        } else {
            return "quiz_create";
        }
    }

    @GetMapping("/createquizRem")
    public String showQuizCreateForm(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String email = "";
            String password = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("emailAdCookie")) {
                    email = cookie.getValue();
                }
                if (cookie.getName().equalsIgnoreCase("passwordAdCookie")) {
                    password = cookie.getValue();
                }

            }
            if (!email.isEmpty() && !password.isEmpty()) {
                System.out.println(email + " " + password);
                return "quiz_list";
            }
        }
        return "index";
    }

    @PostMapping("/createquiz")
    public String createQuiz(Quiz quiz) {
        quizService.createQuiz(quiz);
        // Redirect to question creation page with quiz id as a parameter
        return "redirect:/createquestion?quizId=" + quiz.getId();
    }

    @GetMapping("/quizzesRem")
    public String showQuizzesRem(Model model, HttpSession session, HttpServletRequest request) {

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String email = "";
                String password = "";
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equalsIgnoreCase("emailUsCookie")) {
                        email = cookie.getValue();
                    }
                    if (cookie.getName().equalsIgnoreCase("passwordUsCookie")) {
                        password = cookie.getValue();
                    }

                }
                if (!email.isEmpty() && !password.isEmpty()){
                    List<Quiz> quizzes = quizService.getAllQuizzes();
                    model.addAttribute("quizzes", quizzes);
                    System.out.println(email + " "+password);
                    return "quiz_list";
                }
            }

        return "index";
    }

    @GetMapping("/quizzes")
    public String showQuizzes(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }else {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            model.addAttribute("quizzes", quizzes);
            return "quiz_list";
        }
    }


}


