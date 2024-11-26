package ru.job4j.bmb.recommendation;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jvnet.hk2.annotations.Service;

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
}
