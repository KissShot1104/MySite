package com.mysite.medium.article_vote.service;

import com.mysite.medium.article.entity.Article;
import com.mysite.medium.article.repository.ArticleRepository;
import com.mysite.medium.article.service.ArticleService;
import com.mysite.medium.article_vote.dto.ArticleVoteDto;
import com.mysite.medium.article_vote.entity.ArticleVote;
import com.mysite.medium.article_vote.repository.ArticleVoteRepository;
import com.mysite.medium.user.entity.SiteUser;
import com.mysite.medium.user.repository.UserRepository;
import com.mysite.medium.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleVoteServiceImpl implements ArticleVoteService {

    //어떨때 repository를 부르고 어떨 때 Service를 불러야 하는가?
    private final ArticleVoteRepository articleVoteRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleService articleService;
    private final UserService userService;


    @Transactional
    public void toggleArticleVote(final Long articleId, final String username) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isEmpty()) {
            throw new EntityNotFoundException("article Not Found Entity");
        }

        Optional<SiteUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Not Found Entity");
        }

        Optional<ArticleVote> articleVote = articleVoteRepository.findByArticleIdAndUserId(articleId, user.get().getId());
        if (articleVote.isEmpty()) {
            createArticleVote(article.get(), user.get());
        } else {
            deleteArticleVoteByArticleVote(articleVote.get());
        }
    }

    private void createArticleVote(Article article, SiteUser user) {
        ArticleVote articleVote = ArticleVote.builder()
                .article(article)
                .user(user)
                .build();

        articleVoteRepository.save(articleVote);
    }

    private void deleteArticleVoteByArticleVote(ArticleVote articleVote) {
        articleVoteRepository.delete(articleVote);
    }


    public List<ArticleVoteDto> findArticleVoterAllByArticleId(Long articleId) {
        List<ArticleVote> articleVote = articleVoteRepository.findAllByArticleId(articleId);

        List<ArticleVoteDto> articleVoteDtoList = articleVote.stream()
                .map(this::articleVoterToArticleVoterDto)
                .toList();

        return articleVoteDtoList;
    }

    @Transactional
    public void deleteArticleVoteAllByArticleId(Long articleId) {
        articleVoteRepository.deleteAllByArticleId(articleId);
    }

    private ArticleVoteDto articleVoterToArticleVoterDto(ArticleVote articleVote) {
        ArticleVoteDto articleVoteDto = ArticleVoteDto.builder()
                .id(articleVote.getId())
                .siteUserDto(userService.siteUserToSiteUserDto(articleVote.getUser()))
                .articleDto(articleService.articleToArticleDto(articleVote.getArticle()))
                .build();

        return articleVoteDto;
    }

}
