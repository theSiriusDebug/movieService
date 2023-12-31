package com.example.MovieService.sevices;

import com.example.MovieService.models.Reply;
import com.example.MovieService.repositories.ReplyRepository;
import com.example.MovieService.sevices.interfaces.ReplyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class ReplyServiceImpl implements ReplyService {
    private static final Logger logger = Logger.getLogger(ReplyServiceImpl.class.getName());
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public List<Reply> findAllReplies() {
        logger.info("Retrieving all replies");

        try {
            return replyRepository.findAll();
        } catch (Exception e) {
            logger.info("Failed to retrieve replies");
            throw new RuntimeException("Failed to retrieve replies", e);
        }
    }

    @Override
    public Reply findReplyById(Long id) {
        try {
            logger.info(String.format("Retrieving reply with ID: %d", id));
            return replyRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Reply not found with ID: " + id));
        } catch (NotFoundException e) {
            logger.info(String.format("Reply not found with ID: %d", id));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReply(Reply reply) {
        if (reply == null) {
            throw new IllegalArgumentException("Reply cannot be null");
        }

        try {
            logger.info(String.format("Deleting reply with ID: %d", reply.getId()));
            replyRepository.delete(Objects.requireNonNull(reply, "Reply cannot be null."));
            logger.info("Reply deleted successfully");
        } catch (Exception e) {
            logger.info("Failed to delete reply: " + reply);
            throw new RuntimeException("Failed to delete reply", e);
        }
    }


    @Override
    public void saveReply(Reply reply) {
        if (reply == null) {
            throw new IllegalArgumentException("Reply cannot be null");
        }

        try {
            replyRepository.save(Objects.requireNonNull(reply, "Reply cannot be null."));
            logger.info("Reply saved successfully.");
        } catch (Exception e) {
            logger.info("Failed to save reply: " + reply);
            throw new RuntimeException("Failed to save reply", e);
        }
    }
}
