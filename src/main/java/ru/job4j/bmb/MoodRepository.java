package ru.job4j.bmb;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.Mood;
import java.util.List;

public interface MoodRepository extends CrudRepository<Mood, Long> {
    List<Mood> findAll();
}
