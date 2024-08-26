package com.example.onlinequizapp.user;

import com.example.onlinequizapp.model.User;
import com.example.onlinequizapp.service.LogService;
import com.example.onlinequizapp.service.UserService;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserActionListener implements HttpSessionListener {
        @Autowired
        private LogService logService;

    public void logUserAction(User user, String action) {
        logService.logAction(action, user.getEmail());

    }

    public void logoutAction(String user, String action) {
        logService.logAction(action, user);

    }

    public void quizStartAction(String user, String action) {
        logService.logAction(action, user);

    }

}
