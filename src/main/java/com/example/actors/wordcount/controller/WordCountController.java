package com.example.actors.wordcount.controller;

import com.example.actors.wordcount.actorsystem.ActorWordCount;
import com.example.actors.wordcount.utils.UtilityMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/v1/actors/")
public class WordCountController {

    @Autowired
    private transient ActorWordCount actorWordCount;

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Events Successfully pushed"),
                    @ApiResponse(code = 400, message = "Bad Request"),
                    @ApiResponse(code = 500, message = "Server Error"),
            }
    )
    @PostMapping(
            path = "/word-count",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<Map<String, String>> wordCountActor(
            @ApiParam(value = "Event payload", required = true) @RequestBody String payload
    ) {
        try{
            ConcurrentHashMap<String,Integer> output = new ConcurrentHashMap<>();
            log.info("Processing in separate thread");
            actorWordCount.wordCount(payload);
            Thread.sleep(1000);
            Map<String, String> body = new HashMap<>();
            body.put("message", "Successfully pushed the data to the Actors.");
            return new ResponseEntity<>( body, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Events Successfully pushed"),
                    @ApiResponse(code = 400, message = "Bad Request"),
                    @ApiResponse(code = 500, message = "Server Error"),
            }
    )
    @GetMapping(
            path = "/count"
    )
    public ResponseEntity<?> getWordCountActor(
    ) {
        ConcurrentHashMap<String,Integer> output = new ConcurrentHashMap<>();
        log.info("Processing in separate thread");
        try {
            output = actorWordCount.getCount();
            String body = UtilityMapper.getObjectMapper().writeValueAsString(output);
            Thread.sleep(1000);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(output);
    }
}


