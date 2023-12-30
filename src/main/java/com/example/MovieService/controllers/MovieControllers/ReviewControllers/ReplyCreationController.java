package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Reply;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.UserRepository;
import com.example.MovieService.sevices.ReplyServiceImpl;
import com.example.MovieService.sevices.ReviewServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Api(tags = "ReviewCreationController API")
@RestController
@RequestMapping("/replies")
public class ReplyCreationController {
    private static final Logger logger = Logger.getLogger(ReviewCreationController.class.getName());
    private final UserRepository userRepository;
    private final ReviewServiceImpl reviewServiceImpl;
    private final ReplyServiceImpl replyServiceImpl;

    public ReplyCreationController(UserRepository userRepository, ReviewServiceImpl reviewServiceImpl, ReplyServiceImpl replyServiceImpl) {
        this.userRepository = userRepository;
        this.reviewServiceImpl = reviewServiceImpl;
        this.replyServiceImpl = replyServiceImpl;
    }

    @ApiOperation("Create a reply to a review")
    @PostMapping("/createReply/{reviewId}")
    public ResponseEntity<Reply> createReply(@PathVariable Long reviewId, @RequestBody String replyText) {
        Review parentReview = reviewServiceImpl.findReviewById(reviewId);

        if (parentReview == null) {
            logger.warning("Parent review not found with ID: " + reviewId);
            return ResponseEntity.notFound().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        if(currentUser == null) {
            logger.warning("You're not logged in.");
            return ResponseEntity.notFound().build();
        }

        Reply reply = new Reply();
        reply.setParentReview(parentReview);
        reply.setUser(currentUser);
        reply.setReplyText(replyText);

        parentReview.getReplies().add(reply);
        reviewServiceImpl.saveReview(parentReview);

        logger.info("Reply created successfully for parent review with ID: " + reviewId);
        return ResponseEntity.ok(reply);
    }

    @ApiOperation("Delete a reply")
    @DeleteMapping("/deleteReply/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable Long replyId) {
        Reply reply = replyServiceImpl.findReplyById(replyId);

        if (reply == null) {
            logger.warning("Reply not found with ID: " + replyId);
            return ResponseEntity.badRequest().body("Reply not found.");
        }

        // Check if the current user is the owner of the reply
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        if (!reply.getUser().equals(currentUser)) {
            logger.warning("Unauthorized deletion attempt for reply with ID: " + replyId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete this reply.");
        }

        // Remove the reply from the parent review's reply list
        Review parentReview = reply.getParentReview();
        parentReview.getReplies().remove(reply);
        reviewServiceImpl.saveReview(parentReview);

        replyServiceImpl.deleteReply(reply);

        logger.info("Reply deleted successfully with ID: " + replyId);
        return ResponseEntity.ok("Reply deleted successfully.");
    }

    @GetMapping
    public ResponseEntity<List<Reply>> get_replies(){
        return ResponseEntity.ok(replyServiceImpl.findAllReplies());
    }
}
