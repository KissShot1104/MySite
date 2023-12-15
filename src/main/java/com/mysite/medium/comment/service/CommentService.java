package com.mysite.medium.comment.service;

import com.mysite.medium.comment.dto.CommentDto;
import com.mysite.medium.comment.entity.Comment;
import com.mysite.medium.user.dto.SiteUserDto;
import org.springframework.data.domain.Page;

public interface CommentService {

    Long createComment(Long articleId, CommentDto commentDto, SiteUserDto author);

    Page<CommentDto> findCommentAllByArticleId(int page, Long id);

    CommentDto findCommentByCommendId(Long commentId);

    void modifyComment(Long commentId , CommentDto commentDto);

    void deleteComment(Long commentId);

    void deleteAllByArticleId(Long articleId);

    Comment commentDtoToComment(CommentDto commentDto);

    CommentDto commentToCommentDto(Comment comment);
}
