package com.rhythmpoizon.currencyconverterbot.botapi.handlers;

import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManagerCommandHandler {
    private final TelegramBot telegramBot;

    @Autowired
    public  ManagerCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void handleManagerCommand(long chatId) {
        String adminName = "https://t.me/ppooizj";
        telegramBot.sendMessage(chatId, "Контакт менеджера: "+ adminName);
    }

}
