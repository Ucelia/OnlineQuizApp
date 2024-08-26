package com.example.onlinequizapp.controller;

import com.example.onlinequizapp.model.Question;
import com.example.onlinequizapp.model.Quiz;
import com.example.onlinequizapp.model.Score;
import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.repository.QuestionRepository;
import com.example.onlinequizapp.repository.ScoreRepository;
import com.example.onlinequizapp.repository.UserRepository;
import com.example.onlinequizapp.service.QuestionService;
import com.example.onlinequizapp.repository.QuizRepository;
import com.example.onlinequizapp.service.ScoreService;
import com.example.onlinequizapp.user.UserActionListener;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserActionListener userActionListener;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/createquestion")
    public String showQuestionCreateForm(Model model, Integer quizId, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }else {
            // Pass the quizId to the question create page
            model.addAttribute("quizId", quizId);
            return "question_create";
        }
    }

    @PostMapping ("/createquestion")
    public String createQuestion(Question question, Integer quizId, RedirectAttributes ra) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            question.setQuiz(quiz);
            questionRepository.save(question);
            ra.addFlashAttribute("message", "Question created successfully");
            return "redirect:/createquestion?quizId=" + quiz.getId();
            //return "redirect:/question/create?quizId=" + quiz.getId();
        } else {
            // Handle case when the quiz with given ID is not found
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }
    }
//    public Optional<Question> getQuestionById(Integer id) {
//        return questionRepository.findById(id);
//    }

    @GetMapping("/quizzes/{quizId}")
    public String showQuestions(@PathVariable("quizId") Integer quizId, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }else {
            List<Question> questions = questionService.getQuestionsByQuizId(quizId);
            model.addAttribute("questions", questions);
            model.addAttribute("quizId", quizId);
            //model.addAttribute("answers", answers); // Assuming 'answers' is a variable containing the user's answers

            userActionListener.quizStartAction("User","Quiz started");


            return "question_list";
        }
    }

    @PostMapping("/submitAnswers")
    public String submitAnswers(@RequestParam String answers,
                                @RequestParam("quizId") Integer quizId,
                                @RequestParam("userId") Integer userId,
                                Model model, HttpSession session,
                                RedirectAttributes ra, @RequestParam("score") Integer score) {

        //System.out.println("Answers:" + answers);
        System.out.println("The Score:" + score);
        System.out.println("UserId: " + userId);
        System.out.println("quizId: " + quizId);
        session.setAttribute("userId", userId);
        try {
            // Retrieve the quiz and user objects from the database
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Create a new Score object with the retrieved user and quiz objects
            Score userScore = new Score(user, quiz, score);

            // Save the score in the database
            scoreService.saveScore(userScore);

            // Redirect to the score display page
            ra.addFlashAttribute("score", score);
            return "show_score";
        } catch (Exception e) {
            // Handle exception appropriately
            e.printStackTrace();
            ra.addFlashAttribute("error", "An error occurred while saving the score.");
//            return "redirect:/result/" + userId + "/" + quizId; // Redirect to an appropriate error page or home
            return "show_score";
        }
    }

    @GetMapping("/result")
    public String showUserResult( Model model, HttpSession session) {
        try {
            Integer user = (Integer) session.getAttribute("userId");
            List<Score> scores = scoreService.getScoreByUser(user);
            model.addAttribute("scores", scores);
            //userActionListener.logUserAction(user, "Showed result");
            //session.setAttribute("score", 0);
            return "show_score";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }




    private int calculateScore(String answers, Integer quizId) {
        // Retrieve the list of correct answers for the quiz from the database
        List<Question> quizQuestions = questionRepository.findByQuizId(quizId);

        // Initialize variables to keep track of the total number of questions and correct answers
        int totalQuestions = quizQuestions.size();
        int correctAnswers = 0;
        //String correct;

        // Iterate through each question and compare the user's answer with the correct answer
        for (int i = 0; i < totalQuestions; i++) {
            Question question = quizQuestions.get(i);
            String correctAnswer = question.getCorrectOption(); // Assuming correctOption contains the correct answer

            //correct= correctAnswer;


            // Check if the user's answer matches the correct answer
            if (answers.equals(correctAnswer)) {
                correctAnswers++;
            }
            System.out.println(answers);
            System.out.println(correctAnswers);
            System.out.println(correctAnswer);
        }

        // Calculate the score as a percentage of correct answers
        //int score = (int) Math.round(((double) correctAnswers / totalQuestions) * 100);

        return correctAnswers;
    }


}


