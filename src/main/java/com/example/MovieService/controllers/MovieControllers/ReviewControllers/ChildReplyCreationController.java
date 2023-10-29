package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Reply;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReplyRepository;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

@Api(tags = "ChildReplyCreationController API")
@RestController
@RequestMapping("/replies")
public class ChildReplyCreationController {
    private static final Logger logger = Logger.getLogger(ReviewCreationController.class.getName());
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;

    public ChildReplyCreationController(MovieRepository movieRepository, UserRepository userRepository, ReviewRepository reviewRepository, ReplyRepository replyRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.replyRepository = replyRepository;
    }
    @ApiOperation("Create a reply to a review or another reply")
    @PostMapping("/createChildReply/{parentId}")
    public ResponseEntity<Reply> createChildReply(@PathVariable Long parentId, @RequestBody String replyText) {
        Reply parentReply = replyRepository.findById(parentId).orElse(null);

        if (parentReply == null) {
            logger.warning("Parent reply not found with ID: " + parentId);
            return ResponseEntity.notFound().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        Reply childReply = new Reply();
        childReply.setParentReply(parentReply);
        childReply.setUser(currentUser);
        childReply.setReplyText(replyText);

        parentReply.getChildReplies().add(childReply);
        replyRepository.save(parentReply);

        logger.info("Reply created successfully for parent reply with ID: " + parentId);
        return ResponseEntity.ok(childReply);
    }

    @ApiOperation("Delete a reply and its child replies")
    @DeleteMapping("/deleteChildReply/{replyId}")
    public ResponseEntity<String> deleteChildReply(@PathVariable Long replyId) {
        Reply childReply = replyRepository.findById(replyId).orElse(null);

        if (childReply == null) {
            logger.warning("Reply not found with ID: " + replyId);
            return ResponseEntity.badRequest().body("Reply not found.");
        }

        // Check if the current user is the owner of the reply
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        if (!childReply.getUser().equals(currentUser)) {
            logger.warning("Unauthorized deletion attempt for reply with ID: " + replyId);
            return ResponseEntity.notFound().build();
        }

        // Delete the reply and its child replies recursively
        deleteReplyRecursive(childReply);

        logger.info("Reply and its child replies deleted successfully with ID: " + replyId);
        return ResponseEntity.ok("Reply and its child replies deleted successfully.");
    }

    private void deleteReplyRecursive(Reply reply) {
        // Delete child replies recursively
        for (Reply childReply : reply.getChildReplies()) {
            deleteReplyRecursive(childReply);
        }

        // Remove the reply from its parent reply's child reply list
        if (reply.getParentReply() != null) {
            reply.getParentReply().getChildReplies().remove(reply);
            replyRepository.save(reply.getParentReply());
        }

        replyRepository.delete(reply);
    }

}