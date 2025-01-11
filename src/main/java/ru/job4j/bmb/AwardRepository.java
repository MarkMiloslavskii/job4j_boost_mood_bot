package ru.job4j.bmb;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import java.util.List;

public interface AwardRepository extends CrudRepository<Award, Long> {
    List<Award> findAll();
}
