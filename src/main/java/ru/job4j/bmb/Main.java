package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.services.TgRemoteService;

import java.util.ArrayList;
import java.util.List;

@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner checkEnv(ApplicationContext ctx) {
        return args -> {
            System.out.println(ctx.getEnvironment().getProperty("telegram.bot.name"));
        };
    }

    @Bean
    CommandLineRunner loadDatabase(MoodRepository moodRepository,
                                   MoodContentRepository moodContentRepository,
                                   AwardRepository awardRepository) {
        return args -> {
            if (!moodRepository.findAll().isEmpty()) {
                return;
            }
            loadMoods(moodRepository, moodContentRepository);
            loadAwards(awardRepository);
        };
    }

    private void loadMoods(MoodRepository moodRepository, MoodContentRepository moodContentRepository) {
        var data = new ArrayList<MoodContent>();
        data.add(new MoodContent(new Mood("Счастливейший на свете 😎", true), "Невероятно! Вы сияете от счастья, продолжайте радоваться жизни."));
        data.add(new MoodContent(new Mood("Воодушевленное настроение 🌟", true), "Великолепно! Вы чувствуете себя на высоте. Продолжайте в том же духе."));
        data.add(new MoodContent(new Mood("Успокоение и гармония 🧘‍♂️", true), "Потрясающе! Вы в состоянии внутреннего мира и гармонии."));
        data.add(new MoodContent(new Mood("В состоянии комфорта ☺️", true), "Отлично! Вы чувствуете себя уютно и спокойно."));
        data.add(new MoodContent(new Mood("Легкое волнение 🎈", true), "Замечательно! Немного волнения добавляет жизни краски."));
        data.add(new MoodContent(new Mood("Сосредоточенное настроение 🎯", true), "Хорошо! Ваш фокус на высоте, используйте это время эффективно."));
        data.add(new MoodContent(new Mood("Тревожное настроение 😟", false), "Не волнуйтесь, всё пройдет. Попробуйте расслабиться и найти источник вашего беспокойства."));
        data.add(new MoodContent(new Mood("Разочарованное настроение 😞", false), "Бывает. Не позволяйте разочарованию сбить вас с толку, всё наладится."));
        data.add(new MoodContent(new Mood("Усталое настроение 😴", false), "Похоже, вам нужен отдых. Позаботьтесь о себе и отдохните."));
        data.add(new MoodContent(new Mood("Вдохновенное настроение 💡", true), "Потрясающе! Вы полны идей и энергии для их реализации."));
        data.add(new MoodContent(new Mood("Раздраженное настроение 😠", false), "Попробуйте успокоиться и найти причину раздражения, чтобы исправить ситуацию."));
        moodRepository.saveAll(data.stream().map(MoodContent::getMood).toList());
        List<Mood> moods = data.stream()
                .map(MoodContent::getMood)
                .toList();
        moodRepository.saveAll(moods);
        moodContentRepository.saveAll(data);
    }

    private void loadAwards(AwardRepository awardRepository) {
        var awards = new ArrayList<Award>();
        awards.add(new Award("Смайлик дня", "За 1 день хорошего настроения.", 1));
        awards.add(new Award("Настроение недели", "За 7 последовательных дней хорошего или отличного настроения.", 7));
        awards.add(new Award("Бонусные очки", "За каждые 3 дня хорошего настроения.", 3));
        awards.add(new Award("Персонализированные рекомендации", "После 5 дней хорошего настроения.", 5));
        awards.add(new Award("Достижение 'Солнечный луч'", "За 10 дней непрерывного хорошего настроения.", 10));
        awards.add(new Award("Виртуальный подарок", "После 15 дней хорошего настроения.", 15));
        awards.add(new Award("Титул 'Лучезарный'", "За 20 дней хорошего или отличного настроения.", 20));
        awards.add(new Award("Доступ к премиум-функциям", "После 30 дней хорошего настроения.", 30));
        awards.add(new Award("Участие в розыгрыше призов", "За каждую неделю хорошего настроения.", 7));
        awards.add(new Award("Эксклюзивный контент", "После 25 дней хорошего настроения.", 25));
        awards.add(new Award("Награда 'Настроение месяца'", "За поддержание хорошего или отличного настроения в течение целого месяца.", 30));
        awards.add(new Award("Физический подарок", "После 60 дней хорошего настроения.", 60));
        awards.add(new Award("Коучинговая сессия", "После 45 дней хорошего настроения.", 45));
        awards.add(new Award("Разблокировка мини-игр", "После 14 дней хорошего настроения.", 14));
        awards.add(new Award("Персональное поздравление", "За значимые достижения (например, 50 дней хорошего настроения).", 50));
        awardRepository.saveAll(awards);
    }
}

