package ru.job4j.bmb;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;

import java.util.List;

public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {
    List<MoodContent> findAll();
}
