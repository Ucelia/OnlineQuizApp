package com.example.onlinequizapp.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public  class AuthenticationFilter implements Filter {
    private static final int MAX_INACTIVE_INTERVAL = 1800;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        boolean loggedIn = session != null && session.getAttribute("user") != null ;
        boolean root = requestURI.endsWith("/") || requestURI.endsWith("/OnlineQuizApp");
        boolean signupRequest = requestURI.endsWith("/register") || requestURI.endsWith("/OnlineQuizApp/register");
        boolean loginRequest = requestURI.endsWith("/login") || requestURI.endsWith("/OnlineQuizApp/login");
        //boolean adminPageRequest = requestURI.endsWith("/create") ;
        //get the last time
        long lastAccessedTime = session != null ? session.getLastAccessedTime() : 0;
        // Get the current time
        long currentTime = System.currentTimeMillis();
        // Calculate the elapsed time since the last access
        long elapsedTime = currentTime - lastAccessedTime;

        if (loggedIn || loginRequest ||signupRequest || root) {
            if (session != null && elapsedTime > MAX_INACTIVE_INTERVAL * 1000) {
                session.invalidate();
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
                return;
            }
            chain.doFilter(request, response);
        }
        else
        {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
        }
    }
}
