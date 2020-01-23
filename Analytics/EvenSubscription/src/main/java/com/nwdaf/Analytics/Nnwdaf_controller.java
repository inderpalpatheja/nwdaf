package com.nwdaf.Analytics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;
import java.net.URI;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/nnwdaf-eventssubscription/v1")
public class Nnwdaf_controller {

    @Autowired
    Nnwdaf_repository repository;

    @GetMapping("/test")
    public String testing()
    { return "Working Properly!"; }


    @PostMapping("/subscriptions")
    public ResponseEntity<String> subscribeNF(@RequestBody NnwdafEventsSubscription user) throws SQLIntegrityConstraintViolationException, URISyntaxException {

       // if(repository.findById(user.getEventID()) != null)
       // { return new ResponseEntity<String>("Duplicate Entry ID." + user.getEventID(), HttpStatus.IM_USED); }

        repository.subscribeNF(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();

       // URI location = new URI(user.getNotificationURI());
       // HttpHeaders responseHeaders = new HttpHeaders();
       // responseHeaders.setLocation(location);
       // responseHeaders.set("MyResponseHeader", "MyValue");
       // return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping("/subscriptions/{subscriptionID}")
    public ResponseEntity<?> updateNF(@PathVariable("subscriptionID") String id, @RequestBody NnwdafEventsSubscription user)
    {
        NnwdafEventsSubscription check_user = repository.findById(id);

        if(check_user == null)
        { return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT); }

        repository.updateNF(user, id);

        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }



    @DeleteMapping(value="/subscriptions/{subscriptionID}")
    public ResponseEntity<?> unsubscribeNF(@PathVariable("subscriptionID") String id)
    {
        NnwdafEventsSubscription user = repository.findById(id);

        if(user == null)
        { return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND); }

        repository.unsubscribeNF(id);

        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
    }




    @GetMapping("/{subscriptionID}")
    public ResponseEntity<?> getNF(@PathVariable("subscriptionID") String id)
    {
        NnwdafEventsSubscription user = repository.findById(id);

        if(user == null)
        { return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND); }

        return new ResponseEntity<NnwdafEventsSubscription>(user, HttpStatus.OK);
    }

}
