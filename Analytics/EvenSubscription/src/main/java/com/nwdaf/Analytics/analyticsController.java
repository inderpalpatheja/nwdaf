package com.nwdaf.Analytics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
public class analyticsController  {

	JdbcTemplate jdbcTemplate;
	

    @Autowired
    analyticsRepository analyticsRepository;
	
    @GetMapping("/dataSet")
   // @RequestMapping("value="dataSet/{eventId}",method=RequestMapping.GET)
    public List<analytics> getAllUsers()
    
    {
    	System.out.println("sad");
    	return analyticsRepository.getUser(); 
    
    }

    @GetMapping("/test")
    public String test()
    { return "testing"; }
    
  
    	
    
    
    
  
    
    @GetMapping("/dataSet/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Integer id)
    {
        analytics analytics = analyticsRepository.findById(id);

        if(analytics == null)
        { return new ResponseEntity<String>("No user Found with the ID " + id, HttpStatus.NOT_FOUND); }

        return new ResponseEntity<analytics>(analytics, HttpStatus.OK);
    }
    
    
    
  
    
    
  
    
    
}

