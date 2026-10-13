package dev.danvega.init;

import dev.danvega.model.Article;
import dev.danvega.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleInitializer implements CommandLineRunner {

    private static final int TOTAL_ARTICLES = 1_000;
    private static final int BATCH_SIZE = 1_000;

    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) {
        if (articleRepository.count() > 0) {
            return;
        }

        log.info("Initializing {} articles", TOTAL_ARTICLES);

        for (int start = 1; start <= TOTAL_ARTICLES; start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE - 1, TOTAL_ARTICLES);

            List<Article> batch = IntStream.rangeClosed(start, end)
                    .mapToObj(this::createArticle)
                    .toList();

            articleRepository.saveAll(batch);

            if (end % 100_000 == 0) {
                log.info("Initialized {}/{} articles", end, TOTAL_ARTICLES);
            }
        }

        log.info("Finished initializing articles");
    }

    private Article createArticle(int i) {
        Article article = new Article();
        article.setTitle("Article " + i + " - Technology News");
        article.setSlug("article-" + i);
        article.setContent("This is the content of article " + i);
        article.setAuthor("Author " + ((i % 20) + 1));
        article.setPublished(i % 2 == 0);
        return article;
    }
}
