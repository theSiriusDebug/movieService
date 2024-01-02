package com.example.MovieService.sevices;

import com.example.MovieService.models.Reply;
import com.example.MovieService.repositories.ReplyRepository;
import com.example.MovieService.sevices.interfaces.ReplyService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public List<Reply> findAllReplies() {
        log.info("Return all replies");
        return replyRepository.findAll();
    }

    @Override
    public Reply findReplyById(Long id) {
        try {
            Reply reply = replyRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Reply not found with ID: " + id));
            log.info("Retrieved reply with ID: {}", reply.getId());
            return reply;
        } catch (NotFoundException e) {
            log.error(String.format("Reply not found with ID: %d", id));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReply(Reply reply) {
        log.info(String.format("Deleting reply with ID: %d", reply.getId()));
        replyRepository.delete(Objects.requireNonNull(reply, "Reply cannot be null."));
        log.info("Reply deleted successfully");
    }


    @Override
    public void saveReply(Reply reply) {
        replyRepository.save(Objects.requireNonNull(reply, "Reply cannot be null."));
        log.info("Reply saved successfully.");
    }
}
