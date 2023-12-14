package com.mysite.medium.article_vote.entity;

import com.mysite.medium.article.entity.Article;
import com.mysite.medium.user.entity.SiteUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVote {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="article_vote_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name="site_user_id")
    private SiteUser user;

}
