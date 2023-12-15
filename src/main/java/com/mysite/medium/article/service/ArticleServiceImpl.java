package com.mysite.medium.article.service;

import com.mysite.medium.article.entity.Article;
import com.mysite.medium.article.dto.ArticleDto;
import com.mysite.medium.article.repository.ArticleRepository;
import com.mysite.medium.comment.service.CommentService;
import com.mysite.medium.user.dto.SiteUserDto;
import com.mysite.medium.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.medium.DataNotFoundException;
import com.mysite.medium.user.entity.SiteUser;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;

    public Page<ArticleDto> getArticleAll(final int page, final String kw) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.articleRepository.findAllByKeyword(kw, pageable);
    }

    public ArticleDto findArticleByArticleId(final Long id) {
        final Optional<Article> article = this.articleRepository.findById(id);

        if (article.isEmpty()) {
            throw new DataNotFoundException("article not found");
        }

        final ArticleDto articleDto = articleToArticleDto(article.get());

        return articleDto;
    }

    @Transactional
    public void createArticle(ArticleDto articleDto, SiteUserDto siteUserDto) {

        SiteUser siteUser = userService.siteUserFormToSiteUser(siteUserDto);

        Article article = Article.builder()
                .subject(articleDto.getSubject())
                .content(articleDto.getContent())
                .author(siteUser)
                .build();

        this.articleRepository.save(article);
    }

    @Transactional
    public void modifyArticle(Long articleId, ArticleDto articleDto) {

        Optional<Article> article = articleRepository.findById(articleId);

        if (article.isEmpty()) {
            throw new DataNotFoundException("article not found");
        }

        article.get().modifyArticle(articleDto);
    }
    
    public void deleteArticle(Long articleId) {

        Optional<Article> article = articleRepository.findById(articleId);

        if (article.isEmpty()) {
            throw new DataNotFoundException("article not found");
        }

        this.articleRepository.delete(article.get());
    }

    @Transactional
    public void voteArticle(Long articleId, SiteUserDto siteUserDto) {

        Optional<Article> articlie = articleRepository.findById(articleId);

        if (articlie.isEmpty()) {
            throw new DataNotFoundException("article not found");
        }

        this.articleRepository.save(articlie.get());
    }

    public ArticleDto articleToArticleDto(Article article) {
        ArticleDto articleDto = ArticleDto.builder()
                .id(article.getId())
                .subject(article.getSubject())
                .content(article.getContent())
                .createDate(article.getCreateDate())
                .modifyDate(article.getModifyDate())
                .author(userService.siteUserToSiteUserForm(article.getAuthor()))
                .build();

        return articleDto;
    }

}