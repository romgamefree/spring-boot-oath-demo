package dev.danvega.init;

import dev.danvega.model.Article;
import dev.danvega.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleInitializer implements CommandLineRunner {

    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) {
        if (articleRepository.count() > 0) {
            return;
        }

        List<Article> articles = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> {
                    Article article = new Article();
                    article.setTitle("Article " + i);
                    article.setSlug("article-" + i);
                    article.setContent("This is the content of article " + i);
                    article.setAuthor("Admin");
                    article.setPublished(i % 2 == 0);
                    return article;
                })
                .toList();
        log.info("Init " + articles.size() + " articles");
        articleRepository.saveAll(articles);
    }
}