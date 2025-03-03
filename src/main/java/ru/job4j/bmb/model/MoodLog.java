package ru.job4j.bmb.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "mb_mood_log")
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mood_id", nullable = false)
    private Mood mood;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    public MoodLog() {
    }

    public MoodLog(User user, Mood mood) {
        this.user = user;
        this.mood = mood;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoodLog moodLog = (MoodLog) o;
        return Objects.equals(id, moodLog.id)
                && Objects.equals(user, moodLog.user)
                && Objects.equals(mood, moodLog.mood)
                && Objects.equals(createdAt, moodLog.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, mood, createdAt);
    }
}
