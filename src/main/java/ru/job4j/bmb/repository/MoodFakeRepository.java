package ru.job4j.bmb.repository;

import ru.job4j.bmb.model.Mood;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MoodFakeRepository implements MoodRepository {

    private Map<Long, Mood> memory = new HashMap<>();

    @Override
    public List<Mood> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public Mood save(Mood mood) {
        memory.put(mood.getId(), mood);
        return mood;
    }

    @Override
    public void deleteById(Long id) {
        memory.remove(id);
    }

    @Override
    public Mood findById(Long id) {
        return memory.get(id);
    }
}
