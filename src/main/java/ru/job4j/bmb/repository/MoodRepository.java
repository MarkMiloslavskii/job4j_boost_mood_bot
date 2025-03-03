package ru.job4j.bmb.repository;

import ru.job4j.bmb.model.Mood;
import java.util.List;

public interface MoodRepository {
    List<Mood> findAll();
    Mood save(Mood mood);
    void deleteById(Long id);
    Mood findById(Long id);
}
