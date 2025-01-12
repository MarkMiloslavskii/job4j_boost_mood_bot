package ru.job4j.bmb.recommendation;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jvnet.hk2.annotations.Service;
import ru.job4j.bmb.content.Content;

@Service
public class RecommendationEngine {

    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine инициализирован.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine уничтожен.");
    }

    public Content recommendFor(long chatId, Long moodId) {
        return null;
    }
}
