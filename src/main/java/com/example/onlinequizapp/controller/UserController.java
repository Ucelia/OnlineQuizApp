package com.example.onlinequizapp.controller;

import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.user.UserActionListener;
import com.example.onlinequizapp.user.UserNotFoundException;
import com.example.onlinequizapp.service.UserService;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserActionListener userActionListener;


    @GetMapping("/users")
    public String showUserList(Model model){
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

//    @GetMapping("/users/new")
//    public String showNewForm(Model model){
//        model.addAttribute("user", new User());
//        model.addAttribute("pageTitle", "Add New User");
//        return "user_form";
//    }


    @GetMapping("/register")
    public String showregForm(Model model){
        return "registration_form";
    }


    @GetMapping("/AdminDashboard")
    public String showadminDash (Model model){
        return "AdminDashboard";
    }


    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        session.invalidate();
        userActionListener.logoutAction("User","Logged out");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showloginForm(Model model){
        return "login_form";
    }

//    @GetMapping("/login")
//    public ModelAndView login(){
//        ModelAndView mav = new ModelAndView("login_form");
//        mav.addObject("user", new User());
//        return mav;
//    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam(value = "staff", required = false) boolean isStaff, RedirectAttributes ra, HttpSession session,
                        @RequestParam(required = false) String remember, Model model, HttpServletResponse response){
        User authUser = service.authenticateByEmail(email, password);
        if(authUser != null) {
            if(isStaff && authUser.isStaff()) {
                session.setAttribute("loggedInUser", authUser);
                //session.setMaxInactiveInterval(5);
                userActionListener.logUserAction(authUser, "Admin Logged in");
                if(remember!=null){

                    Cookie cookie = new Cookie("emailAdCookie",authUser.getEmail().trim());
                    Cookie cookie2 = new Cookie("passwordAdCookie",authUser.getPassword().trim());
                    cookie.setMaxAge(3600);
                    cookie2.setMaxAge(3600);
                    response.addCookie(cookie);
                    response.addCookie(cookie2);
                }

                return "redirect:/createquiz";
            } else {
                // Set the logged in user in the session
                session.setAttribute("loggedInUser", authUser);
                session.setMaxInactiveInterval(1800);
                userActionListener.logUserAction(authUser, "user Logged in");
                if(remember!=null){
                    System.out.println("remember me asdf");
                    Cookie cookie = new Cookie("emailUsCookie",authUser.getEmail().trim());
                    Cookie cookie2 = new Cookie("passwordUsCookie",authUser.getPassword().trim());
                    cookie.setMaxAge(3600);
                    cookie2.setMaxAge(3600);
                    response.addCookie(cookie);
                    response.addCookie(cookie2);
                }

                return "redirect:/quizzes";
            }
        } else {
            ra.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String saveUser(User user, RedirectAttributes ra){
        service.save(user);
        ra.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/login";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try {
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id +")");
            return "user_form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "The user ID " + id + " has been deleted.");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }



}

