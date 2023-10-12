package com.rhythmpoizon.currencyconverterbot.botapi.handlers;
import java.util.Properties;
import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static com.rhythmpoizon.currencyconverterbot.service.CurrencyConverter.*;
/*
* Добавить объяснение почему курс юаня отличается
*
*
*/
@Component
public class CurrencyCommandHandler {
    private TelegramBot telegramBot;
    String EmodjiChina ="\uD83C\uDDE8\uD83C\uDDF3 ";

    @Autowired
    public void CurrencyCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handleCurrencyCommand(long chatId) throws Exception {
        double rate = currencyCNY() + serviceCommissionRate;
        String rateOfYuan = String.format("%.2f",rate);
        telegramBot.sendMessage(chatId, EmodjiChina + "Курс Юаня на данный момент:  "+ rateOfYuan );
    }
}
