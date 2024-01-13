package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Reply;

import java.util.List;

public interface ReplyService {
    List<Reply> findAllReplies();

    Reply findReplyById(Long id);

    void deleteReply(Reply reply);

    void saveReply(Reply reply);
}
