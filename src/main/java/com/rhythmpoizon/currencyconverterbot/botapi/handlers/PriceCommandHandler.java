package com.rhythmpoizon.currencyconverterbot.botapi.handlers;

import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

import static com.rhythmpoizon.currencyconverterbot.service.CurrencyConverter.*;

@Component
public class PriceCommandHandler {

    private final TelegramBot telegramBot;

    @Autowired
    public PriceCommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public static String TEXT_PRICE = "Введите в чат общую сумму товара  в Юанях ¥ и я отправлю Вам пример расчета.\n" +
            " \n" +
            "*** Выбирайте цену которая без скидки (иногда она ЗАЧЕРКНУТА)." +
            " Poizon показывает скидки только на новых аккаунтах. У нас этих скидок нет.";
    public void sendPhotoToChat(long chatId, String photoCaption, String photoFilePath) {
        // Создаем объект SendPhoto
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId)); // Устанавливаем идентификатор чата
        sendPhoto.setCaption(photoCaption); // Устанавливаем подпись к фото

        // Создаем объект InputFile для загрузки фотографии
        InputFile photoInputFile = new InputFile(new File(photoFilePath));

        // Устанавливаем фотографию
        sendPhoto.setPhoto(photoInputFile);

        try {
            telegramBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void handlePriceCommand(long chatId, double priceInCNY) throws Exception {
        int totalAmount = (int) getTotalAmount(priceInCNY);
        String totalAmountCastString = String.valueOf(totalAmount);
        double rate = currencyCNY() + serviceCommissionRate;
        String orderSummary = "📊 Результат :\n\n" +
                priceInCNY+"¥" +" = "+totalAmountCastString+ "₽\n\n" +
                "🧮 Формула расчета: \n" +
               // "- Цена *"+ rate + "+ (950 + 950 + 999)\n" +
                "- Цена *"+ "- Цена * " + String.format("%.2f", rate) + "+ (доставка по Китаю + Китай-Москва + комиссия сервиса)\n\n" +
                "😎 Готов оформить заказ или хочешь задать вопрос?\n" +
                "Тебе сюда: @ppooizj\n\n" +
                "Для оформления заказа отправь фотографию товара, ссылку и свой размер менеджеру 🤩\n\n"+
                "‼  Менеджер отвечает с 10.00 до 23.00 по МСК ‼";;


        telegramBot.sendMessage(chatId, orderSummary );
    }


}
