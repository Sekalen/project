package com.example.final_project.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final String MY_CHAT_ID = "1230068103"; // Ваш ID из @userinfobot

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("✅ Telegram bot successfully registered!");
        } catch (TelegramApiException e) {
            System.out.println("❌ Error registering Telegram bot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("📩 Получено обновление от Telegram: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            
            System.out.println("💬 Сообщение: '" + text + "' от " + chatId);

            if (text.equals("/start")) {
                sendMessage(chatId, "Привет! Я бот для финального проекта.");
            } else {
                sendMessage(chatId, "Я получил ваше сообщение: " + text);
            }
        }
    }

    public void sendMessage(String text) {
        sendMessage(MY_CHAT_ID, text);
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
            System.out.println("✅ Сообщение отправлено в " + chatId + ": " + text);
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}