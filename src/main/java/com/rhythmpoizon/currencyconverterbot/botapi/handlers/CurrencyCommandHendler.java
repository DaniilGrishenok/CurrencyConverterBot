package com.rhythmpoizon.currencyconverterbot.botapi.handlers;

import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.rhythmpoizon.currencyconverterbot.service.CurrencyConverter.correncyCNY;
@Component
public class CurrencyCommandHendler {
    private TelegramBot telegramBot;


    @Autowired
    public void СurrencyCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handleCurrencyCommand(long chatId) throws Exception {
        double rate = correncyCNY();
        String rateOfYuan = String.format("%.2f",rate);
        telegramBot.sendMessage(chatId, "Курс Юаня на данный момент:  "+rateOfYuan );
    }
}
