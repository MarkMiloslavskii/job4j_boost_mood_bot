package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.content.Content;

@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        try {
            switch (content.getType()) {
                case TEXT -> sendTextMessage(content);
                case AUDIO -> sendAudioMessage(content);
                case PHOTO -> sendPhotoMessage(content);
                default -> throw new SentContentException("Неизвестный тип контента", null);
            }
        } catch (TelegramApiException e) {
            throw new SentContentException("Ошибка при отправке сообщения", e);
        }
    }

    private void sendTextMessage(Content content) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(content.getChatId());
        message.setText(content.getText());
        if (content.getMarkup() != null) {
            message.setReplyMarkup(content.getMarkup());
        }
        execute(message);
    }

    private void sendAudioMessage(Content content) throws TelegramApiException {
        SendAudio audio = new SendAudio();
        audio.setChatId(content.getChatId());
        audio.setAudio(new InputFile(String.valueOf(content.getAudio())));
        if (content.getAudio() == null) {
            throw new SentContentException("Аудиофайл отсутствует", null);
        }
        if (content.getText() != null) {
            audio.setCaption(content.getText());
        }
        execute(audio);
    }

    private void sendPhotoMessage(Content content) throws TelegramApiException {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(content.getChatId());
        photo.setPhoto(new InputFile(String.valueOf(content.getPhoto())));
        if (content.getPhoto() == null) {
            throw new SentContentException("Фото отсутствует", null);
        }
        if (content.getText() != null) {
            photo.setCaption(content.getText());
        }
        execute(photo);
    }

    public class SentContentException extends RuntimeException {
        public SentContentException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}

