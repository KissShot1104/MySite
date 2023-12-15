package com.mysite.medium.comment.service;

import com.mysite.medium.comment.dto.CommentDto;
import com.mysite.medium.comment.entity.Comment;
import com.mysite.medium.comment.repository.CommentRepository;
import com.mysite.medium.user.dto.SiteUserDto;
import com.mysite.medium.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mysite.medium.article.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.mysite.medium.DataNotFoundException;
import com.mysite.medium.article.entity.Article;
import com.mysite.medium.user.entity.SiteUser;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public Page<CommentDto> findCommentAllByArticleId(final int page, final Long id) {
        final List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        final Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        final Page<CommentDto> commentPaging = this.commentRepository.findCommentAll(pageable, id);

        return commentPaging;
    }

    public Long createComment(final Long articleId, final CommentDto commentDto, final SiteUserDto author) {

        final Optional<Article> article = articleRepository.findById(articleId);

        final Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .article(article.get())
                .author(userService.siteUserDtoToSiteUser(author))
                .build();

        this.commentRepository.save(comment);

        return comment.getId();
    }
    
    public CommentDto findCommentByCommendId(final Long commentId) {
        final Optional<Comment> comment = this.commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new DataNotFoundException("comment not found");
        }

        return commentToCommentDto(comment.get());
    }

    @Transactional
    public void modifyComment(final Long commentId , final CommentDto commentDto) {
        final Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new DataNotFoundException("Not Found Comment");
        }

        comment.get().modifyComment(commentDto);
    }

    @Transactional
    public void deleteComment(final Long commentId) {

        final Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new DataNotFoundException("Not Found Comment");
        }

        this.commentRepository.delete(comment.get());
    }

    @Transactional
    public void deleteAllByArticleId(final Long articleId) {
        this.commentRepository.deleteAllByArticleId(articleId);
    }

    public Comment commentDtoToComment(final CommentDto commentDto) {
        final SiteUser author = userService.siteUserDtoToSiteUser(commentDto.getAuthor());

        final Comment comment = Comment.builder()
                .id(commentDto.getId())
                .content(commentDto.getContent())
                .article(commentDto.getArticle())
                .author(author)
                .build();

        return comment;
    }

    public CommentDto commentToCommentDto(final Comment comment) {

        final SiteUserDto author = userService.siteUserToSiteUserDto(comment.getAuthor());

        final CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .article(comment.getArticle())
                .createDate(comment.getCreateDate())
                .modifyDate(comment.getModifyDate())
                .author(author)
                .build();

        return commentDto;
    }

}