package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Reply;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.UserRepository;
import com.example.MovieService.sevices.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Api(tags = "ChildReplyCreationController API")
@RestController
@RequestMapping("/childReplies")
public class ChildReplyCreationController {
    private static final Logger logger = Logger.getLogger(ChildReplyCreationController.class.getName());
    private final UserRepository userRepository;
    private final ReplyService childReplyService;

    @Autowired
    public ChildReplyCreationController(UserRepository userRepository, ReplyService childReplyService) {
        this.userRepository = userRepository;
        this.childReplyService = childReplyService;
    }
    @ApiOperation("Create a reply to a review or another reply")
    @PostMapping("/createChildReply/{parentId}")
    public ResponseEntity<Reply> createChildReply(@PathVariable Long parentId, @RequestBody String replyText) {
        Reply parentReply = childReplyService.findReplyById(parentId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        if(currentUser == null) {
            logger.warning("You're not logged in.");
            return ResponseEntity.notFound().build();
        }

        Reply childReply = new Reply();
        childReply.setParentReply(parentReply);
        childReply.setUser(currentUser);
        childReply.setReplyText(replyText);

        parentReply.getChildReplies().add(childReply);
        childReplyService.saveReply(parentReply);

        logger.info("Child reply created successfully for parent reply with ID: " + parentId);
        return ResponseEntity.ok(childReply);
    }

    @ApiOperation("Delete a child reply and its child replies")
    @DeleteMapping("/deleteChildReply/{childReplyId}")
    public ResponseEntity<String> deleteChildReply(@PathVariable Long childReplyId) {
        Reply childReply = childReplyService.findReplyById(childReplyId);
        // Check if the current user is the owner of the reply
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        if(currentUser == null) {
            logger.warning("You're not logged in.");
            return ResponseEntity.notFound().build();
        }

        // Delete the reply and its child replies recursively
        deleteReplyRecursive(childReply);

        logger.info("Reply and its child replies deleted successfully with ID: " + childReplyId);
        return ResponseEntity.ok("Reply and its child replies deleted successfully.");
    }

    private void deleteReplyRecursive(Reply reply) {
        // Delete child replies recursively
        for (Reply childReply : reply.getChildReplies()) {
            deleteReplyRecursive(childReply);
        }

        // Remove the reply from its parent reply's child reply list
        if (reply.getParentReply() != null) {
            logger.warning("Child reply not null!");
            reply.getParentReply().getChildReplies().remove(reply);
            childReplyService.saveReply(reply.getParentReply());
        }

        logger.info("child_reply deleted successful!");
        childReplyService.deleteReply(reply);
    }

    @GetMapping
    public List<Reply> get_child_replies(){
        logger.info("get all child_replies!");
        return childReplyService.findAllReplies();
    }
}