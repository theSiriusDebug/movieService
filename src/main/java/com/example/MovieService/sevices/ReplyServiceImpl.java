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
        logger.info("Return all replies");
        return replyRepository.findAll();
    }

    @Override
    public Reply findReplyById(Long id) {
        try {
            return replyRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Reply not found with ID: " + id));
        } catch (NotFoundException e) {
            logger.info(String.format("Reply not found with ID: %d", id));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReply(Reply reply) {
        logger.info(String.format("Deleting reply with ID: %d", reply.getId()));
        replyRepository.delete(Objects.requireNonNull(reply, "Reply cannot be null."));
        logger.info("Reply deleted successfully");
    }


    @Override
    public void saveReply(Reply reply) {
        replyRepository.save(Objects.requireNonNull(reply, "Reply cannot be null."));
        logger.info("Reply saved successfully.");
    }
}
