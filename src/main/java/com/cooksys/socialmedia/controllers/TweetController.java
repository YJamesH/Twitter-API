package com.cooksys.socialmedia.controllers;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.services.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    //***********************
    //************GET tweets#100
    //****************['Tweet'] response
    @GetMapping()
    public ResponseEntity<List<TweetResponseDto>> getSortedTweets() {
        List<TweetResponseDto> sortedTweets = tweetService.getSortedTweets();
        if (sortedTweets == null) {
            throw new NotFoundException("Not found");
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sortedTweets, HttpStatus.OK);
    }
    //******************
    //GET tweets/{id}/mentions#88
    //Retrieves the users mentioned in the tweet with the given id.
    // If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

    //Deleted users should be excluded from the response.

    //IMPORTANT Remember that tags and mentions must be parsed by the server!
    //Response ['User']
    @GetMapping("/{id}/mentions")
    public ResponseEntity<List<UserResponseDto>> getMenstionedUsers(@PathVariable("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tweet id is NULL");
        }
        List<UserResponseDto> tweetResponseDtos = tweetService.getMenstionedUsers(id);
        return new ResponseEntity<>(tweetResponseDtos, HttpStatus.OK);
    }


    //********************
    //GET tweets/{id}/context#91
    //Response Context'
    @GetMapping("/{id}/context")
    public ResponseEntity<TweetResponseDto> getTweetContextById(@PathVariable("id") Long id) {
        TweetResponseDto tweetResponseDto = tweetService.getTweetContextById(id);
        return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);
    }

    //***********************
    //*****************POST tweets/{id}/repost#94
    //*************The response should contain the newly-created tweet.
    //************Request 'Credentials'
    //Response 'Tweet'
    @PostMapping("/{id}/repost")
    public ResponseEntity<TweetResponseDto> addRepostToTweet(@PathVariable("id") Long id, @RequestBody CredentialsRequestDto credentialsRequestDto) {
        if ((id == null) || (credentialsRequestDto == null)) {
            //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            throw new BadRequestException("Tweet id  or credentials missing");
             }
        TweetResponseDto tweetResponseDto = tweetService.addRepostToTweet(null, credentialsRequestDto);
        return new ResponseEntity<>(tweetResponseDto, HttpStatus.CREATED);
    }

    //**********************
    //**************DELETE tweets/{id}#97
    //*************Response 'Tweet'
    @DeleteMapping("/{id}")
    public ResponseEntity<TweetResponseDto> deleteTweetById(@PathVariable("id") Long id, @RequestBody CredentialsRequestDto credentialsRequestDto) {
        TweetResponseDto tweetResponseDto = tweetService.deleteTweetById(id, credentialsRequestDto);
        return new ResponseEntity<>(tweetResponseDto, HttpStatus.GONE);
    }
}
