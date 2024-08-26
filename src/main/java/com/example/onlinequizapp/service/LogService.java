package com.example.onlinequizapp.service;

import com.example.onlinequizapp.model.Log;
import com.example.onlinequizapp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void logAction (String action,String email){
        try{
            Log log = new Log();
            log.setUseremail(email);
            log.setAction(action);
            log.setTimestamp(LocalDateTime.now());
            logRepository.save(log);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
