package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Reply;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.ReplyServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "ChildReplyCreationController API")
@RestController
@RequestMapping("/childReplies")
public class ChildReplyCreationController {
    private final UserServiceImpl userServiceImpl;
    private final ReplyServiceImpl childReplyServiceImpl;

    @Autowired
    public ChildReplyCreationController(UserServiceImpl userServiceImpl, ReplyServiceImpl childReplyServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.childReplyServiceImpl = childReplyServiceImpl;
    }
    @GetMapping
    public List<Reply> get_child_replies(){
        log.info("get all child_replies.");
        return childReplyServiceImpl.findAllReplies();
    }

    @ApiOperation("Create a reply to a review or another reply")
    @PostMapping("/createChildReply/{parentId}")
    public ResponseEntity<Reply> createChildReply(@PathVariable Long parentId, @RequestBody String replyText) {
        Reply parentReply = childReplyServiceImpl.findReplyById(parentId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByOptionalUsername(authentication.getName());

        Reply childReply = new Reply();
        childReply.setParentReply(parentReply);
        childReply.setUser(currentUser);
        childReply.setReplyText(replyText);

        parentReply.getChildReplies().add(childReply);
        childReplyServiceImpl.saveReply(parentReply);

        log.info("Child reply created successfully for parent reply with ID: " + parentId);
        return ResponseEntity.ok(childReply);
    }

    @ApiOperation("Delete a child reply and its child replies")
    @DeleteMapping("/deleteChildReply/{childReplyId}")
    public ResponseEntity<String> deleteChildReply(@PathVariable Long childReplyId) {
        Reply childReply = childReplyServiceImpl.findReplyById(childReplyId);
        // Check if the current user is the owner of the reply
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByOptionalUsername(authentication.getName());
        if (!childReply.getUser().equals(currentUser)) {
            log.error("Unauthorized deletion attempt for child reply with ID: " + childReplyId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete this child reply.");
        }

        // Delete the reply and its child replies recursively
        deleteReplyRecursive(childReply);

        log.info("Reply and its child replies deleted successfully with ID: " + childReplyId);
        return ResponseEntity.ok("Reply and its child replies deleted successfully.");
    }

    private void deleteReplyRecursive(Reply reply) {
        // Delete child replies recursively
        for (Reply childReply : reply.getChildReplies()) {
            deleteReplyRecursive(childReply);
        }

        // Remove the reply from its parent reply's child reply list
        if (reply.getParentReply() != null) {
            log.error("Child reply not null!");
            reply.getParentReply().getChildReplies().remove(reply);
            childReplyServiceImpl.saveReply(reply.getParentReply());
        }

        log.info("child_reply deleted successful!");
        childReplyServiceImpl.deleteReply(reply);
    }

    @ApiOperation("Update a reply")
    @PutMapping("/edit/{replyId}")
    public ResponseEntity<String> editReply(@PathVariable Long replyId, @NotBlank @RequestBody String updatedReplyText) {
        log.info("Editing reply with ID: " + replyId);
        Reply reply = childReplyServiceImpl.findReplyById(replyId);

        // Authorization check
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByUsername(authentication.getName());
        if (!reply.getUser().equals(currentUser)) {
            log.error("Unauthorized edit attempt for reply with ID: " + replyId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to edit this reply.");
        }

        // Update the review text
        reply.setReplyText(updatedReplyText);
        childReplyServiceImpl.saveReply(reply);

        log.info("Reply edited successfully with ID: " + replyId);
        return ResponseEntity.ok("Reply updated successfully");
    }
}