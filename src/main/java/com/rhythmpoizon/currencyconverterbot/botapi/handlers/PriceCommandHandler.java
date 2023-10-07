package com.rhythmpoizon.currencyconverterbot.botapi.handlers;

import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import static com.rhythmpoizon.currencyconverterbot.service.CurrencyConverter.getTotalAmount;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class PriceCommandHandler {
    private final TelegramBot telegramBot;

    @Autowired
    public PriceCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void handlePriceCommand(long chatId, double priceInCNY) throws Exception {
        double totalAmount = getTotalAmount(priceInCNY);
        String totalAmountCastString = String.format("%.2f",totalAmount);
        telegramBot.sendMessage(chatId, "Итоговая сумма заказа:  "+ totalAmountCastString );
    }


}
