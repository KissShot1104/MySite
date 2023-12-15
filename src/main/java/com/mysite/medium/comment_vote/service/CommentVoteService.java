package com.mysite.medium.comment_vote.service;

import com.mysite.medium.comment.entity.Comment;
import com.mysite.medium.comment_vote.dto.CommentVoteDto;
import com.mysite.medium.comment_vote.entity.CommentVote;
import com.mysite.medium.user.entity.SiteUser;
import java.util.Map;

public interface CommentVoteService {
    void toggleCommentVote(final Long commentId, final String username);
    void createCommentVote(final Comment comment, final SiteUser user);
    void deleteCommentVoteAllByCommentId(Long commentId);
    void deleteCommentVoteAllByArticleId(Long articleId);
    CommentVoteDto articleVoteToArticleVoteDto(CommentVote commentVote);

    Map<Long, Long> getCommentLikesForArticle(Long articleId);
}
