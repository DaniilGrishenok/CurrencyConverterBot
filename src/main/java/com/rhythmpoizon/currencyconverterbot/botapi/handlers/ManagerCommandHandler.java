package com.rhythmpoizon.currencyconverterbot.botapi.handlers;
import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManagerCommandHandler {
    private final TelegramBot telegramBot;
    String message = "🛍️ Готовы сделать заказ? Наш менеджер всегда готов помочь вам!";
    String orderInstructions = "Чтобы сделать заказ, просто отправьте нашему менеджеру следующую информацию:\n\n" +
            "1. Размер позиции.\n" +
            "2. Ссылку на эту позицию.\n\n" +
            "Наш менеджер свяжется с вами и ответит на все ваши вопросы! 🤝💬";
    String adminName = "https://t.me/ppooizj";
    String managerContact = "📞 Для связи с менеджером просто перейдите по ссылке: \n" +adminName+"\n"+
            "❗ Менеджер отвечает с 10.00 до 23.00 по МСК ❗" ;


    String finalMessage = message + "\n\n"  + orderInstructions + "\n\n"  + managerContact;
    @Autowired
    public  ManagerCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void handleManagerCommand(long chatId) {

        telegramBot.sendMessage(chatId, finalMessage);
    }

}
